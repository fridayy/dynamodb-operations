package one.leftshift.common.mapping.chain;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

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
}
