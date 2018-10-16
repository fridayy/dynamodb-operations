package one.leftshift.common.mapping.chain;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import org.apache.commons.codec.binary.Base64;

import java.util.Objects;
import java.util.stream.Collectors;

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
            return attributeValue.getBS().stream()
                    .map(byteBuffer -> Base64.encodeBase64String(byteBuffer.array()))
                    .collect(Collectors.toList());
        } else {
            return super.handle(attributeValue);
        }
    }
}
