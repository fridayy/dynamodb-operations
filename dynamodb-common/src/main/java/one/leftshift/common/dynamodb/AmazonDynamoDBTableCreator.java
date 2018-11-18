package one.leftshift.common.dynamodb;

import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.CreateTableResult;
import com.amazonaws.services.dynamodbv2.model.DescribeTableResult;

import java.util.function.Supplier;

/**
 * @author benjamin.krenn@leftshift.one - 11/14/18.
 * @since 1.0.0
 */
public interface AmazonDynamoDBTableCreator extends Waitable {
    CreateTableResult create(Supplier<CreateTableRequest> requestSupplier);
    CreateTableResult create(DescribeTableResult anotherTable);
}
