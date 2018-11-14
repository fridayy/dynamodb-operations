package one.leftshift.mirror.service.task;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.*;
import com.amazonaws.waiters.Waiter;
import com.amazonaws.waiters.WaiterParameters;
import com.amazonaws.waiters.WaiterTimedOutException;
import com.amazonaws.waiters.WaiterUnrecoverableException;
import one.leftshift.common.dynamodb.AmazonDynamoDBFactory;
import one.leftshift.common.dynamodb.adapter.CreateTableRequestAdapter;
import one.leftshift.common.dynamodb.exception.TableCreationFailureException;
import one.leftshift.mirror.service.task.exception.UndeterminableStateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author benjamin.krenn@leftshift.one - 10/17/18.
 * @since 1.0.0
 */
public class DefaultMirroringTask implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(DefaultMirroringTask.class);
    private final MirroringContext context;

    public DefaultMirroringTask(MirroringContext context) {
        this.context = context;
    }

    @Override
    public void run() {
        final AmazonDynamoDB fromClient = AmazonDynamoDBFactory.synchronous(this.context.getFrom());
        final AmazonDynamoDB toClient = AmazonDynamoDBFactory.synchronous(this.context.getTo());
        log.info("mirroring {} from {} to {}", this.context.getTableName(), this.context.getFrom().getName(), this.context.getTo().getName());

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
            log.error("table {} does exist in region {}", this.context.getTableName(), this.context.getFrom().getName());
            throw new IllegalArgumentException(this.context.getTableName() + "does not exist on " + this.context.getFrom().getName());
        }
    }

    private void createTable(AmazonDynamoDB toClient, DescribeTableResult fromTable) {
        try {
            toClient.createTable(new CreateTableRequestAdapter(fromTable));
        } catch (Exception e) {
            log.error("could not create " + this.context.getTableName(), e);
            throw new TableCreationFailureException(e);
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
            log.info("{} successfully created in region {}", this.context.getTableName(), this.context.getTo().getName());
        } catch (WaiterUnrecoverableException | WaiterTimedOutException | AmazonServiceException e) {
            log.error("Could not determine if table " + this.context.getTableName() + " has been created.", e);
            throw new UndeterminableStateException(e);
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
            log.info("{} successfully deleted in region {}", this.context.getTableName(), this.context.getTo().getName());
        } catch (WaiterUnrecoverableException | WaiterTimedOutException | AmazonServiceException e) {
            log.error("Could not determine if table " + this.context.getTableName() + " exists.", e);
            throw new UndeterminableStateException(e);

        }
    }

    private void deleteTargetTableIfExists(AmazonDynamoDB toClient) {
        try {
            toClient.deleteTable(this.context.getTableName());
            log.info("Deleting {} on {}.", this.context.getTableName(), this.context.getTo().getName());
        } catch (ResourceNotFoundException e) {
            log.info("{} does not exists. Skipping deletion.", this.context.getTableName());
        } catch (ResourceInUseException | LimitExceededException e) {
            log.error("could not delete " + this.context.getTableName(), e);
            throw new UndeterminableStateException(e);
        }
    }
}
