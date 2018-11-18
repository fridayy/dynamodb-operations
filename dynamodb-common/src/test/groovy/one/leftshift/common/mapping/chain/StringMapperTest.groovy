package one.leftshift.common.mapping.chain

import com.amazonaws.services.dynamodbv2.model.AttributeValue

import java.nio.ByteBuffer

class StringMapperTest extends AbstractAttributeValueMapperChainTest {
    @Override
    AttributeValueMaperChain<Object> ClassUnderTest(AttributeValueMaperChain<Object> next) {
        return new StringMapper(next)
    }

    @Override
    List<AttributeValue> ForwardTypes() {
        return [
                new AttributeValue().withSS(["asdasd"," asdasd"]),
                new AttributeValue().withN("1"),
                new AttributeValue().withNS(["1","2", "3"]),
                new AttributeValue().withB(ByteBuffer.wrap("a".bytes)),
                new AttributeValue().withBS([ByteBuffer.wrap("a".bytes), ByteBuffer.wrap("b".bytes)]),
                new AttributeValue().withL([new AttributeValue().withS("asdasd"), new AttributeValue().withN("1")]),
                new AttributeValue().withNULL(true),
                new AttributeValue().withM(["test": new AttributeValue().withS("asdasd")]),
                new AttributeValue().withBOOL(true)
        ]
    }

    @Override
    List<Tuple2<AttributeValue, Object>> HandleableAttributeValueTypes() {
        return [
                new Tuple2<AttributeValue, Object>(new AttributeValue().withS("asdasd"), "asdasd")
        ]
    }

    @Override
    List<Tuple2<Object, AttributeValue>> HandleableObjectTypes() {
        return [
                new Tuple2<Object, AttributeValue>("hello", new AttributeValue().withS("hello"))
        ]
    }
}
