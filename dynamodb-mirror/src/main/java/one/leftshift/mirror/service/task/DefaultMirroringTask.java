package one.leftshift.mirror.service.task;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.*;
import com.amazonaws.waiters.Waiter;
import com.amazonaws.waiters.WaiterParameters;
import com.amazonaws.waiters.WaiterTimedOutException;
import com.amazonaws.waiters.WaiterUnrecoverableException;
import one.leftshift.mirror.service.adapter.CreateTableRequestAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author benjamin.krenn@leftshift.one - 10/17/18.
 * @since 1.0.0
 */
public class DefaultMirroringTask implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultMirroringTask.class);
    private final MirroringContext context;

    public DefaultMirroringTask(MirroringContext context) {
        this.context = context;
    }

    @Override
    public void run() {
        final AmazonDynamoDB fromClient = createClient(this.context.getFrom());
        final AmazonDynamoDB toClient = createClient(this.context.getTo());
        LOGGER.info("mirroring {} from {} to {}", this.context.getTableName(), this.context.getFrom().getName(), this.context.getTo().getName());

        DescribeTableResult fromTable = describeSourceTable(fromClient);
        deleteTargetTableIfExists(toClient);
        waitForDeletion(toClient);
        createTable(toClient, fromTable);
        waitForCreation(toClient);
    }

    private DescribeTableResult describeSourceTable(AmazonDynamoDB fromClient) {
        try {
            return fromClient.describeTable(this.context.getTableName());
        } catch (ResourceNotFoundException e) {
            LOGGER.error("table {} does exist in region {}", this.context.getTableName(), this.context.getFrom().getName());
            throw new IllegalArgumentException(this.context.getTableName() + "does not exist on " + this.context.getFrom().getName());
        }
    }

    private void createTable(AmazonDynamoDB toClient, DescribeTableResult fromTable) {
        try {
            toClient.createTable(new CreateTableRequestAdapter(fromTable));
        } catch (Exception e) {
            LOGGER.error("could not create " + this.context.getTableName(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Blocks until the table has been successfully created. Throws exception otherwise.
     * @param toClient client for the target region
     */
    private void waitForCreation(AmazonDynamoDB toClient) {
        Waiter<DescribeTableRequest> waiter = toClient.waiters().tableExists();
        try {
            waiter.run(new WaiterParameters<>(new DescribeTableRequest(this.context.getTableName())));
            LOGGER.info("{} successfully created in region {}", this.context.getTableName(), this.context.getTo().getName());
        } catch (WaiterUnrecoverableException | WaiterTimedOutException | AmazonServiceException e) {
            LOGGER.error("Could not determine if table " + this.context.getTableName() + " has been created.", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Blocks until the table has been deleted. Throws exception otherwise.
     * @param toClient client for the target region
     */
    private void waitForDeletion(AmazonDynamoDB toClient) {
        Waiter<DescribeTableRequest> waiter = toClient.waiters().tableNotExists();
        try {
            waiter.run(new WaiterParameters<>(new DescribeTableRequest(this.context.getTableName())));
            LOGGER.info("{} successfully deleted in region {}", this.context.getTableName(), this.context.getTo().getName());
        } catch (WaiterUnrecoverableException | WaiterTimedOutException | AmazonServiceException e) {
            LOGGER.error("Could not determine if table " + this.context.getTableName() + " exists.", e);
            throw new RuntimeException(e);
        }
    }

    private void deleteTargetTableIfExists(AmazonDynamoDB toClient) {
        try {
            toClient.deleteTable(this.context.getTableName());
            LOGGER.info("Deleting {} on {}.", this.context.getTableName(), this.context.getTo().getName());
        } catch (ResourceNotFoundException e) {
            LOGGER.info("{} does not exists. Skipping deletion.", this.context.getTableName());
        } catch (ResourceInUseException | LimitExceededException e) {
            LOGGER.error("could not delete " + this.context.getTableName(), e);
            throw new RuntimeException(e);
        }
    }

    private static AmazonDynamoDB createClient(Regions region) {
        return AmazonDynamoDBClientBuilder.standard().withRegion(region).build();
    }
}
