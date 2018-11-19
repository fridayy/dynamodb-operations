package one.leftshift.common.mapping.chain;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Objects;

/**
 * @author benjamin.krenn@leftshift.one - 10/13/18.
 * @since 1.0.0
 */
class ByteListMapper extends AbstractAttributeValueMapperChain {

    ByteListMapper(AttributeValueMaperChain<Object> next) {
        super(next);
    }

    @Override
    public Object handle(AttributeValue attributeValue) {
        if (Objects.nonNull(attributeValue.getBS())) {
            return attributeValue.getBS();
        } else {
            return super.handle(attributeValue);
        }
    }

    @Override
    public AttributeValue handle(Object object) {
        if (isListOfType(object, ByteBuffer.class)) {
            return new AttributeValue().withBS((List<ByteBuffer>) object);
        }
        return super.handle(object);
    }
}
