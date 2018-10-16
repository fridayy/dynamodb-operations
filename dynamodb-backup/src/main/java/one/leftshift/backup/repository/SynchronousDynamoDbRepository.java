package one.leftshift.backup.repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import one.leftshift.common.mapping.AttributeValueMapper;
import one.leftshift.common.repository.DynamoDbRepository;
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
public class SynchronousDynamoDbRepository implements DynamoDbRepository<Map<String, Object>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SynchronousDynamoDbRepository.class);
    private final AmazonDynamoDB syncDynamoDbClient;
    private final String tableName;

    public SynchronousDynamoDbRepository(AmazonDynamoDB syncDynamoDbClient, String tableName) {
        this.syncDynamoDbClient = syncDynamoDbClient;
        this.tableName = tableName;
    }

    @Override
    public Collection<Map<String, Object>> findAll() {
        try {
            return this.syncDynamoDbClient.scan(new ScanRequest(this.tableName))
                    .getItems()
                    .stream()
                    .map(AttributeValueMapper::map).collect(Collectors.toList());
        } catch (ResourceNotFoundException e) {
            LOGGER.error("Invalid tableName: " + tableName, e);
        }
        return Collections.emptyList();
    }
}
