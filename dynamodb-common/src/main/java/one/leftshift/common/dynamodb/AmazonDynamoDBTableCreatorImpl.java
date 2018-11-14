package one.leftshift.common.dynamodb;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.CreateTableResult;
import com.amazonaws.services.dynamodbv2.model.DescribeTableResult;
import com.amazonaws.services.dynamodbv2.waiters.AmazonDynamoDBWaiters;
import one.leftshift.common.dynamodb.adapter.CreateTableRequestAdapter;
import one.leftshift.common.dynamodb.exception.TableCreationFailureException;

/**
 * @author benjamin.krenn@leftshift.one - 11/14/18.
 * @since 1.0.0
 */
public class AmazonDynamoDBTableCreatorImpl implements AmazonDynamoDBTableCreator {

    private final AmazonDynamoDB synchronousDynamoDB;

    public AmazonDynamoDBTableCreatorImpl(AmazonDynamoDB synchronousDynamoDB) {
        this.synchronousDynamoDB = synchronousDynamoDB;
    }

    @Override
    public CreateTableResult create(String tableName) {
        return null;
    }

    @Override
    public CreateTableResult create(DescribeTableResult anotherTable) {
        try {
            CreateTableResult result = this.synchronousDynamoDB.createTable(new CreateTableRequestAdapter(anotherTable));
            waitFor(anotherTable.getTable().getTableName(), AmazonDynamoDBWaiters::tableExists);
            return result;
        } catch (Exception e) {
            log.error("could not create " + anotherTable.getTable().getTableName(), e);
            throw new TableCreationFailureException(e);
        }
    }

    @Override
    public AmazonDynamoDB getClient() {
        return this.synchronousDynamoDB;
    }
}
