package one.leftshift.common.mapping.chain;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import one.leftshift.common.mapping.AttributeValueMapper;

import java.util.Map;
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
            return AttributeValueMapper.toMap(attributeValue.getM());
        } else {
            return super.handle(attributeValue);
        }
    }

    @Override
    public AttributeValue handle(Object object) {
        if (object instanceof Map) {
            return new AttributeValue().withM(AttributeValueMapper.fromMap((Map<String, Object>) object));
        }
        return super.handle(object);
    }
}
