package no.ssb.jsontemplate.valueproducer;


import no.ssb.jsontemplate.jsonbuild.JsonStringNode;

import java.security.SecureRandom;

/**
 * This class produces a {@link JsonStringNode} which can generate a
 * 6-octet MAC address string.
 */
public class MacValueProducer extends AbstractValueProducer<JsonStringNode> {
    /**
     * The type name used in the template, e.g. {aMacField: @mac}
     */
    public static final String TYPE_NAME = "mac";
    private static final SecureRandom random = new SecureRandom();

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
        return new JsonStringNode(this::produceMac);
    }

    /**
     * Produces an ip string
     *
     * @return ip format string
     */
    protected String produceMac() {
        String[] macParts = new String[]{
                String.format("%02X", (random.nextInt(255) & 0xF0)), //& 0xF0 to keep it a universal mac address
                String.format("%02X", random.nextInt(255)),
                String.format("%02X", random.nextInt(255)),
                String.format("%02X", random.nextInt(255)),
                String.format("%02X", random.nextInt(255)),
                String.format("%02X", random.nextInt(255))
        };
        return String.join(":", macParts);
    }
}

