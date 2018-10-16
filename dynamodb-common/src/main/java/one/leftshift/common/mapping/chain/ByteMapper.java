package one.leftshift.common.mapping.chain;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import org.apache.commons.codec.binary.Base64;

import java.util.Objects;

/**
 * Converts a {@link java.nio.ByteBuffer} to a Base64 encoded string
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
            return Base64.encodeBase64String(attributeValue.getB().array());
        } else {
            return super.handle(attributeValue);
        }
    }
}
