package no.ssb.jsontemplate.valueproducer;

import no.ssb.jsontemplate.jsonbuild.JsonStringNode;

import java.security.SecureRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * This class produces a {@link JsonStringNode} which can generate an
 * ip string.
 */
public class Ipv6ValueProducer extends AbstractValueProducer<JsonStringNode> {
    /**
     * The type name used in the template, e.g. {anIpv6Field: @ipv6}
     */
    public static final String TYPE_NAME = "ipv6";
    private static final SecureRandom random = new SecureRandom();
    private static final String LETTERS = "0123456789abcdef";


    @Override
    public String getTypeName() {
        return TYPE_NAME;
    }

    /**
     * Produces a node which can generate a random ipv6 string
     *
     * @return the produced json string node
     */
    @Override
    public JsonStringNode produce() {
        return new JsonStringNode(this::produceIp);
    }

    /**
     * Produces an ipv6 string
     *
     * @return the produced ipv6 format string
     */
    protected String produceIp() {
        return IntStream.range(0, 8)
                .mapToObj(i -> produceGroup())
                .collect(Collectors.joining(":"));
    }

    private String produceGroup() {
        int length = LETTERS.length();
        char[] group = new char[]{
                LETTERS.charAt(random.nextInt(length)),
                LETTERS.charAt(random.nextInt(length)),
                LETTERS.charAt(random.nextInt(length)),
                LETTERS.charAt(random.nextInt(length))
        };
        return new String(group);
    }
}
