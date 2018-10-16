package one.leftshift.common.util

import spock.lang.Specification
import spock.lang.Unroll

class ObjectUtilTest extends Specification {

    @Unroll
    void "assertNotNull(#objects) throws IllegalArgumentException"() {
        when:
            ObjectUtil.assertNotNull("test", objects)
        then:
            def ex = thrown(IllegalArgumentException)
            ex.message == "test"
        where:
            objects                        || _
            null                           || _
            ["1", null, "3"] as Object[]   || _
            [null, null, null] as Object[] || _
    }

    void "does not throw IllegalArgumentException"() {
        when:
            ObjectUtil.assertNotNull("test", "1", "2")
        then:
            noExceptionThrown()
    }

    @SuppressWarnings("GroovyPointlessBoolean")
    @Unroll
    void "isNull(#objects) returns expected boolean"() {
        when:
            def result = ObjectUtil.hasNull(objects)
        then:
            result == expectation
        where:
            objects               || expectation
            [null] as Object[]    || true
            [1, null] as Object[] || true
            [null, 1] as Object[] || true
            [] as Object[]        || false
            [1, 2, 3] as Object[] || false
            ["hi"] as Object[]    || false
            null                  || true
    }

    @Unroll
    void "firstNonNull(#objects) returns #expectation"() {
        when:
            def result = ObjectUtil.firstNonNull(objects)
        then:
            result == expectation
        where:
            objects                       || expectation
            [null, null, "a"] as Object[] || "a"
            [null, null] as Object[]      || null
            ["a", "b"] as Object[]        || "a"
            ["a", 23] as Object[]         || "a"
            null                          || null

    }
}
