package no.ssb.jsontemplate.valueproducer;

import no.ssb.jsontemplate.jsonbuild.*;

/**
 * This is the default type in JsonTemplate. It produces a value with the type which
 * the value looks like. For example:
 *
 * <ul>
 * <li>"null" results in <i>null</i></li>
 * <li>"true" results in <i>true</i></li>
 * <li>"fAlSe" results in <i>false</i></li>
 * <li>"1" results in <i>1</i></li>
 * <li>"1f" results in <i>1.0</i></li>
 * <li>"hello" results in <i>"hello"</i></li>
 * </ul>
 * <p>
 * The smart type is designed to be used only for the fixed value.
 * For example:
 * <br>
 * {name : @smart(John)} will result in {name : John}
 * <br>
 * However,
 * <br>
 * {name : @smart} will result in {name : null}
 */
public class SmartValueProducer extends AbstractValueProducer<JsonNode> {

    /**
     * The type name used in the template, e.g. @smart{ name: John}
     */
    public static final String TYPE_NAME = "smart";
    private static final String NULL = "null";

    @Override
    public String getTypeName() {
        return TYPE_NAME;
    }

    @Override
    public JsonNode produce() {
        return new JsonNullNode();
    }

    @Override
    public JsonNode produce(String value) {
        if (isNull(value)) {
            return new JsonNullNode();
        }

        Boolean parsedBoolean = parseBoolean(value);
        if (parsedBoolean != null) {
            return new JsonBooleanNode(() -> parsedBoolean);
        }

        Integer parsedInteger = parseInteger(value);
        if (parsedInteger != null) {
            return new JsonIntegerNode(() -> parsedInteger);
        }

        Float parsedFloat = parseFloat(value);
        if (parsedFloat != null) {
            return new JsonFloatNode(() -> parsedFloat);
        }

        return new JsonStringNode(() -> value);
    }

    private boolean isNull(String value) {
        return NULL.equalsIgnoreCase(value);
    }

    private Boolean parseBoolean(String value) {
        if (Boolean.TRUE.toString().equalsIgnoreCase(value) ||
                Boolean.FALSE.toString().equalsIgnoreCase(value)) {
            return Boolean.parseBoolean(value);
        } else {
            return null;
        }
    }

    private Integer parseInteger(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException nfe) {
            return null;
        }
    }

    private Float parseFloat(String value) {
        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException nfe) {
            return null;
        }
    }
}
