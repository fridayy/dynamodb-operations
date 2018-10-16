package one.leftshift.common.mapping.chain

import com.amazonaws.services.dynamodbv2.model.AttributeValue
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

abstract class AbstractAttributeValueMapperChainTest extends Specification {
    @Subject
    AttributeValueMaperChain<Object> classUnderTest

    @Unroll
    void "handles assigned type and returns expected value"() {
        given:
            AttributeValueMaperChain<Object> mockedNext = Mock(AttributeValueMaperChain)
            classUnderTest = ClassUnderTest(mockedNext)
        when:
            def result = classUnderTest.handle(handleableType.getFirst())
        then:
            result == handleableType.getSecond()
            0 * mockedNext.handle(*_)
        where:
            handleableType << HandleableTypes()
    }

    @Unroll
    void "delegates other types to next"() {
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

    abstract AttributeValueMaperChain<Object> ClassUnderTest(AttributeValueMaperChain<Object> next)
    abstract List<AttributeValue> ForwardTypes()
    abstract List<Tuple2<AttributeValue, Object>> HandleableTypes()
}
