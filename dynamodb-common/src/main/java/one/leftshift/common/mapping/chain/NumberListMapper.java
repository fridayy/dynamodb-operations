package one.leftshift.common.mapping.chain;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author benjamin.krenn@leftshift.one - 10/13/18.
 * @since 1.0.0
 */
class NumberListMapper extends AbstractAttributeValueMapperChain {

    NumberListMapper(AttributeValueMaperChain<Object> next) {
        super(next);
    }

    @Override
    public Object handle(AttributeValue attributeValue) {
        if (Objects.nonNull(attributeValue.getNS())) {
            return attributeValue.getNS();
        } else {
            return super.handle(attributeValue);
        }
    }

    @Override
    public AttributeValue handle(Object object) {
        if (isListOfType(object, Number.class)) {
            Object[] objects = ((List) object).stream().map(Object::toString).toArray();
            return new AttributeValue().withNS(Arrays.copyOf(objects, objects.length, String[].class));
        }
        return super.handle(object);
    }
}
