package kr.co.eicn.ippbx.model.enums;

public enum Bool {
    Y(true), N(false);

    private final Boolean value;

    Bool(Boolean value) {
        this.value = value;
    }

    public Boolean getValue() {
        return value;
    }
}
