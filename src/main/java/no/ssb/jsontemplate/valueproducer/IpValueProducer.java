package no.ssb.jsontemplate.valueproducer;

import no.ssb.jsontemplate.jsonbuild.JsonStringNode;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * This class produces a {@link JsonStringNode} which can generate an
 * ipv6 string.
 */
public class IpValueProducer extends AbstractValueProducer<JsonStringNode> {

    /**
     * The type name used in the template, e.g. {anIpField: @ip}
     */
    public static final String TYPE_NAME = "ip";


    @Override
    public String getTypeName() {
        return TYPE_NAME;
    }

    /**
     * Produces a node which can generate a random ip string
     *
     * @return the produced json string node
     */
    @Override
    public JsonStringNode produce() {
        return new JsonStringNode(this::produceIp);
    }

    /**
     * Produces an ip string
     *
     * @return ip format string
     */
    protected String produceIp() {
        int[] ipParts = new int[]{
                ThreadLocalRandom.current().nextInt(255),
                ThreadLocalRandom.current().nextInt(255),
                ThreadLocalRandom.current().nextInt(255),
                ThreadLocalRandom.current().nextInt(255)
        };
        return Arrays.stream(ipParts)
                .mapToObj(Integer::toString)
                .collect(Collectors.joining("."));
    }
}
