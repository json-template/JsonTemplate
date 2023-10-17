package no.ssb.jsontemplate.valueproducer;


import no.ssb.jsontemplate.jsonbuild.JsonStringNode;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * This class produces a {@link JsonStringNode} which generates current ISO8601 timestamp.
 */
public class Iso8601ValueProducer extends AbstractValueProducer<JsonStringNode> {

    /**
     * The type name used in the template, e.g. {anIsoField: @iso8601}
     */
    public static final String TYPE_NAME = "iso8601";

    @Override
    public String getTypeName() {
        return TYPE_NAME;
    }

    /**
     * Produces a node which can generate current ISO8601 timestamp
     *
     * @return the produced json string node
     */
    @Override
    public JsonStringNode produce() {
        return new JsonStringNode(this::produceTs);
    }

    /**
     * Produces an ISO8601 timestamp based on current time
     *
     * @return ISO8601 timestamp string
     */
    protected String produceTs() {
        return Instant.now().toString();
    }

    @Override
    public JsonStringNode produce(Map<String, String> paramMap) {
        Map<String, String> copyParamMap = new HashMap<>(paramMap);

        String dateFormat = pickStringParam(copyParamMap, "format");
        String dateString = pickStringParam(copyParamMap, "date");
        String as = pickStringParam(copyParamMap, "as");

        validateParamMap(copyParamMap);

        if (dateFormat == null) {
            return produce();
        }

        if (as == null){
            as = "date";
        }

        if (as.equalsIgnoreCase("date")) {
            return new JsonStringNode(() -> produceDateString(dateString, dateFormat));
        }

        return new JsonStringNode(() -> produceDateTimeString(dateString, dateFormat));
    }

    protected String produceDateString(String dateString, String dateFormatPattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormatPattern);
        LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);

        return dateTime.format(DateTimeFormatter.ISO_DATE);
    }

    protected String produceDateTimeString(String dateString, String dateFormatPattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormatPattern);
        LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);

        return dateTime.format(DateTimeFormatter.ISO_DATE_TIME);
    }
}

