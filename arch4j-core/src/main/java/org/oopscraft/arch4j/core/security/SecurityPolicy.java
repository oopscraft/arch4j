package org.oopscraft.arch4j.core.security;

public enum SecurityPolicy {

    ANONYMOUS("Anonymous User"),
    AUTHENTICATED("Authenticated User"),
    AUTHORIZED("Authorized User");

    private final String label;

    SecurityPolicy(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }

}
