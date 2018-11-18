package one.leftshift.common.dynamodb.repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.*;
import com.amazonaws.services.dynamodbv2.waiters.AmazonDynamoDBWaiters;
import one.leftshift.common.annotation.ThreadSafe;
import one.leftshift.common.dynamodb.Waitable;
import one.leftshift.common.functional.Functions;
import one.leftshift.common.mapping.AttributeValueMapper;
import one.leftshift.common.util.Maps;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
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
                    .map(AttributeValueMapper::toMap).collect(Collectors.toList()));
        } catch (ResourceNotFoundException e) {
            log.error("Table {} does not exist", this.tableName);
        }
        return Collections.emptyList();
    }

    @Override
    public void delete() {
        try {
            this.syncDynamoDbClient.deleteTable(this.tableName);
            log.info("Waiting for deletion of {}...", this.tableName);
            waitFor(this.tableName, AmazonDynamoDBWaiters::tableNotExists);
            log.info("Deletion of {} complete.", this.tableName);
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
            log.error("table {} does not exist.", this.tableName);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Collection<Map<String, Object>> items) {
        Collection<List<Map<String,Object>>> partitionedItems = Functions.partition(Functions.copy(items), 25);
        AtomicInteger counter = new AtomicInteger(1);
        try {
            partitionedItems.forEach(item -> {
                List<WriteRequest> attributeValues = item.stream()
                        .map(AttributeValueMapper::fromMap)
                        .map(map -> new WriteRequest(new PutRequest(map)))
                        .collect(Collectors.toList());
                log.info("Batch writing {}/{} items", counter.getAndIncrement(),partitionedItems.size());
                BatchWriteItemResult result = this.syncDynamoDbClient.batchWriteItem(Maps.single(this.tableName, attributeValues));

                if (!result.getUnprocessedItems().isEmpty()) {
                    log.info("Found {} unprocessed items. Retrying...", result.getUnprocessedItems().size());
                    retry(result.getUnprocessedItems());
                }
            });
        } catch (Exception e) {
            log.error("Can not batch write", e);
        }
    }

    private void retry(Map<String, List<WriteRequest>> unprocessedItems) {
        try {
            Thread.sleep(5000);
            BatchWriteItemResult result = this.syncDynamoDbClient.batchWriteItem(unprocessedItems);
            if (!result.getUnprocessedItems().isEmpty()) {
                log.info("Found {} unprocessed items. Retrying...", result.getUnprocessedItems().size());
                retry(result.getUnprocessedItems());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public AmazonDynamoDB getClient() {
        return this.syncDynamoDbClient;
    }
}
