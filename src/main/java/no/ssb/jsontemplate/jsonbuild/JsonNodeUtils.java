package no.ssb.jsontemplate.jsonbuild;

import java.util.Arrays;

public final class JsonNodeUtils {

    private JsonNodeUtils() {
    }

    public static String makeIdentation(int count) {
        if (count <= 0) {
            return "";
        }
        char[] spaces = new char[count * 2];
        Arrays.fill(spaces, ' ');
        return new String(spaces);
    }
}
