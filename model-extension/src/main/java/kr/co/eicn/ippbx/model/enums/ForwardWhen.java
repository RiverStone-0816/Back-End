package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

import java.util.Objects;

/**
 *  착신전환구분
 *  (N:착신전환안함, A:항상, B:통화중, C:부재중, T:통화중+부재중)
 */
public enum ForwardWhen implements CodeHasable<String> {
    NONE("N"), ALWAYS("A"), IN_CALLING("B"), OUT("C"), IN_CALLING_OR_OUT("T");

    private String code;

    ForwardWhen(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }

    public static ForwardWhen of (String code) {
        for (ForwardWhen type : ForwardWhen.values()) {
            if (Objects.equals(type.getCode(), code))
                return type;
        }

        return null;
    }
}
