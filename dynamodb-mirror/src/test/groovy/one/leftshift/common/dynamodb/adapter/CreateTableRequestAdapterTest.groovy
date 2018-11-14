package one.leftshift.common.dynamodb.adapter

import com.amazonaws.services.dynamodbv2.model.*
import spock.lang.Specification
import spock.lang.Subject

class CreateTableRequestAdapterTest extends Specification {

    @Subject
    CreateTableRequestAdapter classUnderTest

    void "adapts DescribeTableResult"() {
        when:
            classUnderTest = new CreateTableRequestAdapter(
                    new DescribeTableResult().withTable(new TableDescription()
                            .withAttributeDefinitions([new AttributeDefinition("attribute", "type")])
                            .withKeySchema([new KeySchemaElement("key", "type")])
                            .withProvisionedThroughput(new ProvisionedThroughputDescription().withReadCapacityUnits(5L).withWriteCapacityUnits(10L))
                            .withSSEDescription(new SSEDescription().withSSEType(SSEType.AES256).withStatus(status))
                    )
            )
        then:
            classUnderTest.getAttributeDefinitions() == [new AttributeDefinition("attribute", "type")]
            classUnderTest.getKeySchema() == [new KeySchemaElement("key", "type")]
            classUnderTest.getProvisionedThroughput().readCapacityUnits == 5L
            classUnderTest.getProvisionedThroughput().writeCapacityUnits == 10L
            classUnderTest.getSSESpecification().getSSEType() == SSEType.AES256.toString()
            classUnderTest.getSSESpecification().enabled == expectation
        where:
            status     || expectation
            "ENABLED"  || Boolean.TRUE
            "ENABLING" || Boolean.TRUE
            "SADHJASD" || Boolean.FALSE
            null       || Boolean.FALSE
    }
}
