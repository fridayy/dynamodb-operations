package one.leftshift.common.dynamodb.repository

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.model.AttributeValue
import com.amazonaws.services.dynamodbv2.model.PutRequest
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException
import com.amazonaws.services.dynamodbv2.model.ScanResult
import com.amazonaws.services.dynamodbv2.model.WriteRequest
import spock.lang.Specification
import spock.lang.Subject

import java.nio.ByteBuffer

/**
 * @author benjamin.krenn@leftshift.one - 11/19/18.
 * @since 1.0.0
 */
class SynchronousDynamoDBRepositoryTest extends Specification {

    @Subject
    SynchronousDynamoDBRepository classUnderTest

    AmazonDynamoDB mockedAmazonDynamoDB

    void setup() {
        mockedAmazonDynamoDB = Mock(AmazonDynamoDB)
        classUnderTest = new SynchronousDynamoDBRepository(mockedAmazonDynamoDB, "xyx")
    }

    void "findAll() returns an empty list if tableName is invalid"() {
        given:
            mockedAmazonDynamoDB.scan(_) >> { throw new ResourceNotFoundException() }
        when:
            def result = classUnderTest.findAll()
        then:
            result == []
    }

    void "findAll() returns expected datastructure"() {
        given:
            mockedAmazonDynamoDB.scan(_) >> new ScanResult()
                    .withItems([["id": new AttributeValue().withS("x6ds5x")], ["name": new AttributeValue().withS("test")]])
        when:
            def result = classUnderTest.findAll()
        then:
            result == [
                    ["id": "x6ds5x"],
                    ["name": "test"]
            ]
    }

    void "save() invokes batchWrite with expected values"() {
        when:
            classUnderTest.save([
                    ["partitionKey": "1337", "sortingKey": "7331", "data": [0x1] as byte[]],
                    ["partitionKey": "1", "sortingKey": "2", "data": [0x3] as byte[]],
            ])
        then:
            1 * mockedAmazonDynamoDB.batchWriteItem({ Map<String, List<WriteRequest>> map ->
                map.containsKey("xyx")
                map["xyx"] == [
                        new WriteRequest(new PutRequest([
                                "partitionKey": new AttributeValue().withS("1337"),
                                "sortingKey"  : new AttributeValue().withS("7331"),
                                "data"        : new AttributeValue().withB(ByteBuffer.wrap([0x1] as byte[]))
                        ])),
                        new WriteRequest(new PutRequest([
                                "partitionKey": new AttributeValue().withS("1"),
                                "sortingKey"  : new AttributeValue().withS("2"),
                                "data"        : new AttributeValue().withB(ByteBuffer.wrap([0x3] as byte[]))
                        ]))]
            } as Map<String, List<WriteRequest>>)
    }

    void "delete() invokes delete and waits for deletion to finish"() {

    }
}
