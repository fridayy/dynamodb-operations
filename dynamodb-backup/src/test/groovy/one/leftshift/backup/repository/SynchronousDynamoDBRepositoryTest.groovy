package one.leftshift.backup.repository

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.model.AttributeValue
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException
import com.amazonaws.services.dynamodbv2.model.ScanResult
import one.leftshift.common.dynamodb.repository.SynchronousDynamoDBRepository
import spock.lang.Specification
import spock.lang.Subject

class SynchronousDynamoDBRepositoryTest extends Specification {

    @Subject
    SynchronousDynamoDBRepository classUnderTest

    AmazonDynamoDB mockedAmazonDynamoDB

    void setup() {
        mockedAmazonDynamoDB = Mock(AmazonDynamoDB)
        classUnderTest = new SynchronousDynamoDBRepository(mockedAmazonDynamoDB, "xyx")
    }

    void "returns an empty list if tableName is invalid"() {
        given:
            mockedAmazonDynamoDB.scan(_) >> { throw new ResourceNotFoundException() }
        when:
            def result = classUnderTest.findAll()
        then:
            result == []
    }

    void "returns expected datastructure"() {
        given:
            mockedAmazonDynamoDB.scan(_) >> new ScanResult()
                    .withItems([["id": new AttributeValue().withS("x6ds5x")], ["name" : new AttributeValue().withS("test")]])
        when:
            def result = classUnderTest.findAll()
        then:
            result == [
                    ["id" : "x6ds5x"],
                    ["name" : "test"]
            ]
    }

}
