package one.leftshift.common.mapping

import com.amazonaws.services.dynamodbv2.model.AttributeValue
import spock.lang.Specification

import java.nio.ByteBuffer

class AttributeValueMapperTest extends Specification {
    void "map returns the expected output"() {
        given:
            Map<String, AttributeValue> attributeValueMap =
                    [
                            "string_field"     : new AttributeValue().withS("stringValue"),
                            "bool_field"       : new AttributeValue().withBOOL(true),
                            "byte_field"       : new AttributeValue().withB(ByteBuffer.wrap("IlikeByteBuffers!".bytes)),
                            "number_field"     : new AttributeValue().withN("1234.45"),
                            "string_list_field": new AttributeValue().withSS("first", "second", "third"),
                            "number_list_field": new AttributeValue().withNS("1", "2", "3"),
                            "byte_list_field"  : new AttributeValue().withBS(ByteBuffer.wrap("firstBuffer".bytes), ByteBuffer.wrap("secondBuffer".bytes)),
                            "toMap"              : new AttributeValue().withM(["Name": new AttributeValue().withS("John")]),
                            "list"             : new AttributeValue().withL(new AttributeValue().withS("something"), new AttributeValue().withN("23.5"), new AttributeValue().withBOOL(false)),
                            "nested"           : new AttributeValue().withM(["first": new AttributeValue().withM(["a": new AttributeValue().withS("b")])]),
                            "nested_list"      : new AttributeValue().withL(new AttributeValue().withL(new AttributeValue().withS("hi")))

                    ]
        when:
            def result = AttributeValueMapper.toMap(attributeValueMap)
        then:
            result == Expectation()
    }


    private static Map<String, Object> Expectation() {
        return [
                "string_field"     : "stringValue",
                "bool_field"       : true,
                "byte_field"       : ByteBuffer.wrap("IlikeByteBuffers!".bytes),
                "number_field"     : "1234.45",
                "string_list_field": ["first", "second", "third"],
                "number_list_field": ["1", "2", "3"],
                "byte_list_field"  : [ByteBuffer.wrap("firstBuffer".bytes), ByteBuffer.wrap("secondBuffer".bytes)],
                "toMap"              : ["Name": "John"],
                "list"             : ["something", "23.5", false],
                "nested"           : ["first": ["a": "b"]],
                "nested_list"      : [["hi"]]
        ]
    }
}
