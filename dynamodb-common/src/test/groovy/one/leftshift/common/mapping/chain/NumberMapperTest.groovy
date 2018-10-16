package one.leftshift.common.mapping.chain

import com.amazonaws.services.dynamodbv2.model.AttributeValue

import java.nio.ByteBuffer

class NumberMapperTest extends AbstractAttributeValueMapperChainTest {
    @Override
    AttributeValueMaperChain<Object> ClassUnderTest(AttributeValueMaperChain<Object> next) {
        return new NumberMapper(next)
    }

    @Override
    List<AttributeValue> ForwardTypes() {
        return [
                new AttributeValue().withS("asdasd"),
                new AttributeValue().withSS(["asdasd"," asdasd"]),
                new AttributeValue().withNS(["1","2", "3"]),
                new AttributeValue().withB(ByteBuffer.wrap("a".bytes)),
                new AttributeValue().withBS([ByteBuffer.wrap("a".bytes), ByteBuffer.wrap("b".bytes)]),
                new AttributeValue().withL([new AttributeValue().withS("asdasd"), new AttributeValue().withN("1")]),
                new AttributeValue().withNULL(true),
                new AttributeValue().withM(["test": new AttributeValue().withS("asdasd")])
        ]
    }

    @Override
    List<Tuple2<AttributeValue, Object>> HandleableTypes() {
        return [
                new Tuple2<AttributeValue, Object>(new AttributeValue().withN("1"), "1")
        ]
    }
}
