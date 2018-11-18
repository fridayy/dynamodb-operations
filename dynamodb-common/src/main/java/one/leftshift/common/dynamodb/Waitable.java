package one.leftshift.common.dynamodb;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.model.DescribeTableRequest;
import com.amazonaws.services.dynamodbv2.waiters.AmazonDynamoDBWaiters;
import com.amazonaws.waiters.Waiter;
import com.amazonaws.waiters.WaiterParameters;
import com.amazonaws.waiters.WaiterTimedOutException;
import com.amazonaws.waiters.WaiterUnrecoverableException;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author benjamin.krenn@leftshift.one - 11/14/18.
 * @since 1.0.0
 */
public interface Waitable extends AmazonDynamoDBAware {

    default void waitFor(String tableName, Function<AmazonDynamoDBWaiters, Waiter<DescribeTableRequest>> waiterFunc) {
        Objects.requireNonNull(getClient());
        Waiter<DescribeTableRequest> waiter = waiterFunc.apply(getClient().waiters());
        try {
            waiter.run(new WaiterParameters<>(new DescribeTableRequest(tableName)));
        } catch (WaiterUnrecoverableException | WaiterTimedOutException | AmazonServiceException e) {
            throw new RuntimeException(e);
        }
    }
}
