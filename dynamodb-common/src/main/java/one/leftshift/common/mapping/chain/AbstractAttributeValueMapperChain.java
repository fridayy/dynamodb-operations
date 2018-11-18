package one.leftshift.common.mapping.chain;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.List;
import java.util.Objects;

/**
 * Chain of responsibility that maps an {@link com.amazonaws.services.dynamodbv2.model.AttributeValue} to
 * a {@link String} representation.
 *
 * @author benjamin.krenn@leftshift.one - 10/13/18.
 * @since 1.0.0
 */
public abstract class AbstractAttributeValueMapperChain implements AttributeValueMaperChain<Object> {

    private final AttributeValueMaperChain<Object> next;

    public AbstractAttributeValueMapperChain(AttributeValueMaperChain<Object> next) {
        this.next = next;
    }

    public Object handle(AttributeValue attributeValue) {
        if (Objects.nonNull(next)) {
            return next.handle(attributeValue);
        }
        throw new IllegalStateException("Could not determine a mapper for " + attributeValue.toString());
    }

    @Override
    public AttributeValue handle(Object object) {
        if (Objects.nonNull(next)) {
            return next.handle(object);
        }
        throw new IllegalStateException("Could not determine a mapper for " + object.getClass().getSimpleName());
    }

    protected static boolean isListOfType(Object object, Class<?> clazz) {
        if (object instanceof List) {
            if (((List) object).isEmpty()) {
                return false;
            }
            Object listItem = ((List) object).get(0);

            return clazz.isInstance(listItem);
        }
        return false;
    }
}
