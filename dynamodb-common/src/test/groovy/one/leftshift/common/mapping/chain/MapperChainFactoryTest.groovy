package one.leftshift.common.mapping.chain

import spock.lang.Specification

class MapperChainFactoryTest extends Specification {

    void cleanup() {
        MapperChainFactory.evictChain()
    }

    void "returns a chain of responsibility"() {
        expect:
            def result = MapperChainFactory.chain()
            result != null
            result instanceof AttributeValueMaperChain<Object>
    }

    void "caches the mapper chain"() {
        expect:
            MapperChainFactory.mapper.get() == null
            def result = MapperChainFactory.chain()
            MapperChainFactory.mapper.get() == result

    }
}
