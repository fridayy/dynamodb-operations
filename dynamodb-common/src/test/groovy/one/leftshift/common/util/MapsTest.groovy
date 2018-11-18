package one.leftshift.common.util

import spock.lang.Specification

/**
 * @author benjamin.krenn@leftshift.one - 11/19/18.
 * @since 1.0.0
 */
class MapsTest extends Specification {

    void "creates expected map"() {
        when:
            def result = Maps.single("key", "value")
        then:
            result["key"] == "value"
    }

    void "created map is immutable"() {
        when:
            def result = Maps.single("key", "value")
            result.put("a", "b")
        then:
            thrown(UnsupportedOperationException)
    }
}
