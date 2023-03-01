package no.ssb.jsontemplate.jsonbuild;

/**
 * This class represents a producer of a json null value.
 */
public final class JsonNullNode implements JsonValueNode {

    @Override
    public String compactString() {
        return "null";
    }

    @Override
    public String prettyString(int indentation) {
        return compactString();
    }
}
