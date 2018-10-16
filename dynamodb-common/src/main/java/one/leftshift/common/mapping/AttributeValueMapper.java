package one.leftshift.common.mapping;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import one.leftshift.common.mapping.chain.AttributeValueMaperChain;
import one.leftshift.common.mapping.chain.MapperChainFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Maps a {@link Map<String,AttributeValue>} to a {@link Map<String,Object>} by
 * invoking a chain of responsibility extracting the unknown values.
 *
 * @author benjamin.krenn@leftshift.one - 10/12/18.
 * @since 1.0.0
 */
public class AttributeValueMapper {

    private AttributeValueMapper() {
        throw new UnsupportedOperationException("can not instantiate " + AttributeValueMapper.class.getSimpleName());
    }

    public static Map<String, Object> map(Map<String, AttributeValue> attributeValueMap) {
        AttributeValueMaperChain<Object> chain = MapperChainFactory.chain();
        final Map<String, Object> returnMap = new HashMap<>();
        attributeValueMap.forEach((key, value) -> returnMap.put(key, chain.handle(value)));
        return returnMap;
    }

}
