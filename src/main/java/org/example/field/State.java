package org.example.field;

public enum State {
    EMPTY(" "),
    SHIP(" ", "0"),
    HIT("X"),
    MISS("."),
    DEAD("X");

    private final String publicValue;
    private final String privateValue;

    State(String value) {
        this.publicValue = value;
        this.privateValue = value;
    }

    State(String publicValue, String privateValue) {
        this.publicValue = publicValue;
        this.privateValue = privateValue;
    }

    public String getPublicValue() {
        return this.publicValue;
    }

    public String getPrivateValue() {
        return this.privateValue;
    }
}
