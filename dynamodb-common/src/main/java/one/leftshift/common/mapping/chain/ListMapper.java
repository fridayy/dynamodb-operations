package one.leftshift.common.mapping.chain;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author benjamin.krenn@leftshift.one - 10/13/18.
 * @since 1.0.0
 */
class ListMapper extends AbstractAttributeValueMapperChain {


    ListMapper(AttributeValueMaperChain<Object> next) {
        super(next);
    }

    @Override
    public Object handle(AttributeValue attributeValue) {
        if (Objects.nonNull(attributeValue.getL())) {
            return attributeValue.getL().stream()
                    .map(value -> MapperChainFactory.chain().handle(value))
                    .collect(Collectors.toList());
        } else {
            return super.handle(attributeValue);
        }
    }

    @Override
    public AttributeValue handle(Object object) {
        if (object instanceof List) {
            Object[] objects = ((List) object).stream().map(value -> MapperChainFactory.chain().handle(value)).toArray();
            return new AttributeValue().withL(
                    Arrays.copyOf(objects, objects.length, AttributeValue[].class)
            );
        }
        return super.handle(object);
    }
}
