package one.leftshift.common.dynamodb;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;

/**
 * @author benjamin.krenn@leftshift.one - 11/14/18.
 * @since 1.0.0
 */
public final class AmazonDynamoDBFactory {

    private AmazonDynamoDBFactory() {
        throw new UnsupportedOperationException();
    }

    public static AmazonDynamoDB synchronous(Regions region) {
        return AmazonDynamoDBClientBuilder.standard().withRegion(region).build();
    }
}
