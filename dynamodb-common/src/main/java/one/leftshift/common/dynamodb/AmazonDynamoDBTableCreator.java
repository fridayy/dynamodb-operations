package one.leftshift.common.dynamodb;

import com.amazonaws.services.dynamodbv2.model.CreateTableResult;
import com.amazonaws.services.dynamodbv2.model.DescribeTableResult;

/**
 * @author benjamin.krenn@leftshift.one - 11/14/18.
 * @since 1.0.0
 */
public interface AmazonDynamoDBTableCreator extends Waitable {
    CreateTableResult create(String tableName);
    CreateTableResult create(DescribeTableResult anotherTable);
}
