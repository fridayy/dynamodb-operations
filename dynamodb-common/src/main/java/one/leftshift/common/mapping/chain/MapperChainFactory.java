package one.leftshift.common.mapping.chain;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Creates the chain of responsibility if not created beforehand.
 *
 * @author benjamin.krenn@leftshift.one - 10/13/18.
 * @since 1.0.0
 */
public final class MapperChainFactory {

    private static AtomicReference<AttributeValueMaperChain<Object>> mapper = new AtomicReference<>();

    private MapperChainFactory() {
        throw new UnsupportedOperationException(MapperChainFactory.class.getSimpleName() + " can not be instantiated.");
    }

    public static AttributeValueMaperChain<Object> chain() {
        AttributeValueMaperChain<Object> initializedChain = mapper.get();
        if (Objects.isNull(initializedChain)) {
            //@formatter:off
            return mapper.updateAndGet(t ->
                    new BoolMapper(
                    new ByteListMapper(
                    new ByteMapper(
                    new NumberListMapper(
                    new NumberMapper(
                    new StringListMapper(
                    new StringMapper(
                    new MapMapper(
                    new ListMapper(null))))))))));
        }
        //@formatter:on
        return initializedChain;
    }

    public static void evictChain() {
        mapper.set(null);
    }
}
