package no.ssb.jsontemplate.modelbuild;

enum Token {

    /**
     * variables in template are started with $
     */
    VARIABLE("$"),
    /**
     * raw strings in template are wrapped with `
     */
    RAW("`"),
    /**
     * types in template are started with @
     */
    TYPE("@");

    private final String tag;

    Token(String tag) {
        this.tag = tag;
    }

    String getTag() {
        return tag;
    }
}
