package one.leftshift.common.mapping.chain;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.nio.ByteBuffer;
import java.util.Objects;

/**
 * @author benjamin.krenn@leftshift.one - 10/13/18.
 * @since 1.0.0
 */
class ByteMapper extends AbstractAttributeValueMapperChain {

    ByteMapper(AttributeValueMaperChain<Object> next) {
        super(next);
    }

    @Override
    public Object handle(AttributeValue attributeValue) {
        if (Objects.nonNull(attributeValue.getB())) {
            return attributeValue.getB();
        } else {
            return super.handle(attributeValue);
        }
    }

    @Override
    public AttributeValue handle(Object object) {
        if (object instanceof byte[]) {
            return new AttributeValue().withB(ByteBuffer.wrap((byte[]) object));
        }
        if (object instanceof ByteBuffer) {
            return new AttributeValue().withB((ByteBuffer) object);
        }
        return super.handle(object);
    }
}
