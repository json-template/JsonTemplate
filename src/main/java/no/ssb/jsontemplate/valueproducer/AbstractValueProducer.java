package no.ssb.jsontemplate.valueproducer;

import no.ssb.jsontemplate.jsonbuild.JsonNode;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class AbstractValueProducer<T extends JsonNode> implements IValueProducer<T> {

    @Override
    public T produce() {
        throw new UnsupportedOperationException();
    }

    @Override
    public T produce(String value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T produce(List<String> valueList) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T produce(Map<String, String> paramMap) {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the integer value from the paramMap based on the paramName.
     * The value is then removed from the map.
     *
     * @param paramMap  parameter map
     * @param paramName name of the value
     * @return the integer value referred by the paramName
     */
    protected Integer pickIntegerParam(Map<String, String> paramMap, String paramName) {
        return pickParamValue(paramMap, paramName, Integer::parseInt);
    }

    /**
     * Returns the float value from the paramMap based on the paramName.
     * The value is then removed from the map.
     *
     * @param paramMap  parameter map
     * @param paramName name of the value
     * @return the float value referred by the paramName
     */
    protected Float pickFloatParam(Map<String, String> paramMap, String paramName) {
        return pickParamValue(paramMap, paramName, Float::parseFloat);
    }

    /**
     * Returns the boolean value from the paramMap based on the paramName.
     * The value is then removed from the map.
     *
     * @param paramMap  parameter map
     * @param paramName name of the value
     * @return the boolean value referred by the paramName
     */
    protected Boolean pickBooleanParam(Map<String, String> paramMap, String paramName) {
        return pickParamValue(paramMap, paramName, Boolean::parseBoolean);
    }

    /**
     * Returns the string value from the paramMap based on the paramName.
     * The value is then removed from the map.
     *
     * @param paramMap  parameter map
     * @param paramName name of the value
     * @return the string value referred by the paramName
     */
    protected String pickStringParam(Map<String, String> paramMap, String paramName) {
        return paramMap.get(paramName);
    }

    private <R> R pickParamValue(Map<String, String> paramMap, String paramName, Function<String, R> parser) {
        String paramValue = paramMap.remove(paramName);
        if (paramValue != null) {
            return parser.apply(paramValue);
        } else {
            return null;
        }
    }

    /**
     * Returns a random integer in the range of min and max.
     *
     * @param min minimal bound
     * @param max maximal bound
     * @return random value between min and max
     */
    protected int randomIntInRange(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    /**
     * Returns a random float in the range of min and max.
     *
     * @param min minimal bound
     * @param max maximal bound
     * @return random value between min and max
     */
    protected float randomFloatInRange(float min, float max) {
        return min + ThreadLocalRandom.current().nextFloat() * (max - min);
    }

    /**
     * Validates if the map has values. If it has, that means it contains
     * values which are not supported. In this case, an
     * {@link IllegalArgumentException IllegalArgumentExcpetion} is thrown.
     *
     * @param paramMap parameter map
     */
    protected void validateParamMap(Map<String, String> paramMap) {
        if (paramMap.size() > 0) {
            String unexpectedArgument = paramMap.keySet().stream().collect(Collectors.joining(", "));
            throw new IllegalArgumentException("Arguments [" + unexpectedArgument + "] is not supported in " + this.getClass().getName());
        }
    }

    protected void shouldBePositive(int number, String fieldName) {
        if (number < 0) {
            throw new IllegalArgumentException("[" + fieldName + "] should be positive.");
        }
    }

    protected void shouldBeInAscOrder(int min, int max, String field1, String field2) {
        if (min > max) {
            throw new IllegalArgumentException("[" + field1 + "] should be less than [" + field2 + "].");
        }
    }

    protected void shouldBeInAscOrder(float min, float max, String field1, String field2) {
        if (min > max) {
            throw new IllegalArgumentException("[" + field1 + "] should be less than [" + field2 + "].");
        }
    }
}
