package one.leftshift.common.functional

import spock.lang.Specification
import spock.lang.Unroll

import java.util.function.Function

class FunctionsTest extends Specification {

    void "does collapse"() {
        given:
            List<Function<Map<String, Object>, Map<String, Object>>> functions = [
                    new Function<Map<String, Object>, Map<String, Object>>() {
                        @Override
                        Map<String, Object> apply(Map<String, Object> stringObjectMap) {
                            stringObjectMap.replace("a", 1)
                            return new HashMap<String, Object>(stringObjectMap)
                        }
                    },
                    new Function<Map<String, Object>, Map<String, Object>>() {
                        @Override
                        Map<String, Object> apply(Map<String, Object> stringObjectMap) {
                            stringObjectMap.replace("b", 2)
                            return new HashMap<String, Object>(stringObjectMap)
                        }
                    },
            ]
        when:
            def result = Functions.collapse(functions).apply(["a": 0, "b": 1])
        then:
            result == ["a": 1, "b": 2]
    }

    void "foldsLeft"() {
        given:
            List<Integer> ints = [1, 2, 3, 4, 5]
        when:
            def result = Functions.foldLeft(ints, "0", new Function<String, Function<Integer, String>>() {
                @Override
                Function<Integer, String> apply(String s) {
                    return new Function<Integer, String>() {
                        @Override
                        String apply(Integer integer) {
                            return "($s + $integer)"
                        }
                    }
                }
            })
        then:
            result == "(((((0 + 1) + 2) + 3) + 4) + 5)"
    }

    @Unroll
    void "partition(#inputList , #partitionSize).size() == #expectation"() {
        when:
            def result = Functions.partition(inputList, partitionSize)
        then:
            result.size() == expectation
        where:
            inputList                | partitionSize || expectation
            [1]                      | 0             || 1
            [1, 2, 3]                | 0             || 1
            [1]                      | -1            || 1
            [1]                      | 10            || 1
            [1, 2, 3, 4, 5, 6, 7, 8] | 2             || 4
    }

    void "partition returns expected result"() {
        when:
            def result = Functions.partition([1, 2, 3, 4, 5, 6, 7, 8], 2)
        then:
            result[0] == [1,2]
            result[1] == [3,4]
            result[2] == [5,6]
            result[3] == [7,8]
    }

    void "creates list"() {
        when:
            def result = Functions.list("a", "b", "c")
        then:
            result == ["a", "b", "c"]
    }

    @Unroll
    void "throws exception if trying to add null values"() {
        when:
            Functions.list(input)
        then:
            thrown(Exception)
        where:
            input << [["a", null] as Object[], [null] as Object[], null]
    }
}
