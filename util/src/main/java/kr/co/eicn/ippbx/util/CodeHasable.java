package kr.co.eicn.ippbx.util;

import com.fasterxml.jackson.annotation.JsonValue;

public interface CodeHasable<T> {
    @JsonValue
    T getCode();
}
