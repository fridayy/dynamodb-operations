package one.leftshift.common.dynamodb.repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.*;
import com.amazonaws.services.dynamodbv2.waiters.AmazonDynamoDBWaiters;
import one.leftshift.common.annotation.ThreadSafe;
import one.leftshift.common.dynamodb.Waitable;
import one.leftshift.common.mapping.AttributeValueMapper;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author benjamin.krenn@leftshift.one - 10/16/18.
 * @since 1.0.0
 */
@ThreadSafe
public class SynchronousDynamoDBRepository implements DynamoDBRepository<Map<String, Object>>, Waitable {

    private static final Logger log = LoggerFactory.getLogger(SynchronousDynamoDBRepository.class);
    private final AmazonDynamoDB syncDynamoDbClient;
    private final String tableName;

    public SynchronousDynamoDBRepository(AmazonDynamoDB syncDynamoDbClient, String tableName) {
        this.syncDynamoDbClient = syncDynamoDbClient;
        this.tableName = tableName;
    }

    @Override
    public Collection<Map<String, Object>> findAll() {
        try {
            return Collections.unmodifiableList(this.syncDynamoDbClient.scan(new ScanRequest(this.tableName))
                    .getItems()
                    .stream()
                    .map(AttributeValueMapper::map).collect(Collectors.toList()));
        } catch (ResourceNotFoundException e) {
            log.error("Table {} does not exist", this.tableName);
        }
        return Collections.emptyList();
    }

    @Override
    public void delete() {
        try {
            this.syncDynamoDbClient.deleteTable(this.tableName);
            waitFor(this.tableName, AmazonDynamoDBWaiters::tableNotExists);
        } catch (ResourceNotFoundException e) {
            log.info("{} does not exists. Skipping deletion.", this.tableName);
        } catch (ResourceInUseException | LimitExceededException e) {
            log.error("could not delete " + this.tableName, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    @Nullable
    public DescribeTableResult describe() {
        try {
            return this.syncDynamoDbClient.describeTable(this.tableName);
        } catch (ResourceNotFoundException e) {
            log.error("table {} does exist.", this.tableName);
        }
        return null;
    }


    @Override
    public AmazonDynamoDB getClient() {
        return this.syncDynamoDbClient;
    }
}
