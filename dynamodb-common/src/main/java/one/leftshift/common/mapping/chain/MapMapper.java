package one.leftshift.common.mapping.chain;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import one.leftshift.common.mapping.AttributeValueMapper;

import java.util.Objects;

/**
 * @author benjamin.krenn@leftshift.one - 10/13/18.
 * @since 1.0.0
 */
class MapMapper extends AbstractAttributeValueMapperChain {

    MapMapper(AttributeValueMaperChain<Object> next) {
        super(next);
    }

    @Override
    public Object handle(AttributeValue attributeValue) {
        if (Objects.nonNull(attributeValue.getM())) {
            return AttributeValueMapper.map(attributeValue.getM());
        } else {
            return super.handle(attributeValue);
        }
    }
}
