package one.leftshift.common.dynamodb;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.CreateTableResult;
import com.amazonaws.services.dynamodbv2.model.DescribeTableResult;
import com.amazonaws.services.dynamodbv2.waiters.AmazonDynamoDBWaiters;
import one.leftshift.common.dynamodb.adapter.CreateTableRequestAdapter;
import one.leftshift.common.dynamodb.exception.TableCreationFailureException;

import java.util.function.Supplier;

/**
 * @author benjamin.krenn@leftshift.one - 11/14/18.
 * @since 1.0.0
 */
public class DefaultAmazonDynamoDBTableCreator implements AmazonDynamoDBTableCreator {

    private final AmazonDynamoDB synchronousDynamoDB;

    public DefaultAmazonDynamoDBTableCreator(AmazonDynamoDB synchronousDynamoDB) {
        this.synchronousDynamoDB = synchronousDynamoDB;
    }

    @Override
    public CreateTableResult create(DescribeTableResult anotherTable) {
        return create(() -> new CreateTableRequestAdapter(anotherTable));
    }

    @Override
    public CreateTableResult create(Supplier<CreateTableRequest> requestSupplier) {
        CreateTableRequest request = requestSupplier.get();
        try {
            CreateTableResult result = this.synchronousDynamoDB.createTable(request);
            waitFor(request.getTableName(), AmazonDynamoDBWaiters::tableExists);
            return result;
        } catch (Exception e) {
            throw new TableCreationFailureException(e);
        }
    }

    @Override
    public AmazonDynamoDB getClient() {
        return this.synchronousDynamoDB;
    }
}
