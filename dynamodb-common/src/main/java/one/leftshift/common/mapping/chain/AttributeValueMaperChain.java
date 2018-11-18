package one.leftshift.common.mapping.chain;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

/**
 * @author benjamin.krenn@leftshift.one - 10/16/18.
 * @since 1.0.0
 */
public interface AttributeValueMaperChain<T> {

    T handle(AttributeValue attributeValue);

    AttributeValue handle(T object);
}
