package one.leftshift.common.mapping.chain;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.List;
import java.util.Objects;

/**
 * @author benjamin.krenn@leftshift.one - 10/13/18.
 * @since 1.0.0
 */
class StringListMapper extends AbstractAttributeValueMapperChain {

    StringListMapper(AttributeValueMaperChain<Object> next) {
        super(next);
    }

    @Override
    public Object handle(AttributeValue attributeValue) {
        if (Objects.nonNull(attributeValue.getSS())) {
            return attributeValue.getSS();
        } else {
            return super.handle(attributeValue);
        }
    }

    @Override
    public AttributeValue handle(Object object) {
        if (isListOfType(object, String.class)) {
            return new AttributeValue().withSS((List<String>) object);
        }
        return super.handle(object);
    }
}
