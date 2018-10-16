package one.leftshift.common.mapping.chain;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.Objects;

/**
 * @author benjamin.krenn@leftshift.one - 10/13/18.
 * @since 1.0.0
 */
class BoolMapper extends AbstractAttributeValueMapperChain {

    BoolMapper(AttributeValueMaperChain<Object> next) {
        super(next);
    }

    @Override
    public Object handle(AttributeValue attributeValue) {
        if (Objects.nonNull(attributeValue.getBOOL())) {
            return attributeValue.getBOOL();
        } else {
            return super.handle(attributeValue);
        }
    }
}
