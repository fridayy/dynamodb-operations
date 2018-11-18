package one.leftshift.common.mapping.chain;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.Objects;

/**
 * @author benjamin.krenn@leftshift.one - 10/13/18.
 * @since 1.0.0
 */
class StringMapper extends AbstractAttributeValueMapperChain {

    StringMapper(AttributeValueMaperChain<Object> next) {
        super(next);
    }

    @Override
    public Object handle(AttributeValue attributeValue) {
        if (Objects.nonNull(attributeValue.getS())) {
            return attributeValue.getS();
        } else {
            return super.handle(attributeValue);
        }
    }

    @Override
    public AttributeValue handle(Object object) {
        if (object instanceof String) {
            return new AttributeValue().withS((String) object);
        }
        return super.handle(object);
    }
}
