package one.leftshift.common.dynamodb;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;

/**
 * @author benjamin.krenn@leftshift.one - 11/14/18.
 * @since 1.0.0
 */
public interface AmazonDynamoDBAware {
    AmazonDynamoDB getClient();
}
