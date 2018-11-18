package one.leftshift.common.mapping.chain

import com.amazonaws.services.dynamodbv2.model.AttributeValue

import java.nio.ByteBuffer

class MapMapperTest extends AbstractAttributeValueMapperChainTest {
    @Override
    AttributeValueMaperChain<Object> ClassUnderTest(AttributeValueMaperChain<Object> next) {
        return new MapMapper(next)
    }

    @Override
    List<AttributeValue> ForwardTypes() {
        return [
                new AttributeValue().withS("asdasd"),
                new AttributeValue().withSS(["asdasd", " asdasd"]),
                new AttributeValue().withN("1"),
                new AttributeValue().withNS(["1", "2", "3"]),
                new AttributeValue().withB(ByteBuffer.wrap("a".bytes)),
                new AttributeValue().withBS([ByteBuffer.wrap("a".bytes), ByteBuffer.wrap("b".bytes)]),
                new AttributeValue().withL([new AttributeValue().withS("asdasd"), new AttributeValue().withN("1")]),
                new AttributeValue().withNULL(true),
                new AttributeValue().withBOOL(false)
        ]
    }

    @Override
    List<Tuple2<AttributeValue, Object>> HandleableAttributeValueTypes() {
        return [
                new Tuple2<AttributeValue, Object>(new AttributeValue().withM(["test": new AttributeValue().withS("asdasd")]), ["test": "asdasd"]),
                new Tuple2<AttributeValue, Object>(new AttributeValue().withM(["test": new AttributeValue().withM(["nested" : new AttributeValue().withS("hi")])]), ["test": ["nested" : "hi"]])
        ]
    }

    @Override
    List<Tuple2<Object, AttributeValue>> HandleableObjectTypes() {
        return [
                new Tuple2<Object, AttributeValue>(
                        ["a": false],
                        new AttributeValue().withM(["a" : new AttributeValue().withBOOL(false)])
                )
        ]
    }
}
