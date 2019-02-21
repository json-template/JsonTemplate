package com.github.jsontemplate.modelbuild;

enum Token {

    /**
     * variables in template are started with $
     */
    VARIABLE("$"),
    /**
     * raw strings in template are wrapped with `
     */
    RAW("`");

    private String tag;

    Token(String tag) {
        this.tag = tag;
    }

    String getTag() {
        return tag;
    }
}
