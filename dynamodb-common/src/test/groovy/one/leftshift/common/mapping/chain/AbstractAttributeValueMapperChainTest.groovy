package one.leftshift.common.mapping.chain

import com.amazonaws.services.dynamodbv2.model.AttributeValue
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

abstract class AbstractAttributeValueMapperChainTest extends Specification {
    @Subject
    AttributeValueMaperChain<Object> classUnderTest

    @Unroll
    void "handles assigned AttributeValue type and returns expected value"() {
        given:
            AttributeValueMaperChain<Object> mockedNext = Mock(AttributeValueMaperChain)
            classUnderTest = ClassUnderTest(mockedNext)
        when:
            def result = classUnderTest.handle(handleableType.getFirst())
        then:
            result == handleableType.getSecond()
            0 * mockedNext.handle(*_)
        where:
            handleableType << HandleableAttributeValueTypes()
    }

    @Unroll
    void "delegates other AttributeValue types to next"() {
        given:
            AttributeValueMaperChain<Object> mockedNext = Mock(AttributeValueMaperChain)
            classUnderTest = ClassUnderTest(mockedNext)
        when:
            def result = classUnderTest.handle(forwardType)
        then:
            1 * mockedNext.handle(*_)
            result == null
        where:
            forwardType << ForwardTypes()
    }

    @Unroll
    void "handles assigned Object type and returns expected value"() {
        given:
            AttributeValueMaperChain<Object> mockedNext = Mock(AttributeValueMaperChain)
            classUnderTest = ClassUnderTest(mockedNext)
        when:
            def result = classUnderTest.handle(objectType.getFirst())
        then:
            result == objectType.getSecond()
            0 * mockedNext.handle(*_)
        where:
            objectType << HandleableObjectTypes()
    }

    abstract AttributeValueMaperChain<Object> ClassUnderTest(AttributeValueMaperChain<Object> next)

    abstract List<AttributeValue> ForwardTypes()

    abstract List<Tuple2<AttributeValue, Object>> HandleableAttributeValueTypes()

    abstract List<Tuple2<Object, AttributeValue>> HandleableObjectTypes()
}
