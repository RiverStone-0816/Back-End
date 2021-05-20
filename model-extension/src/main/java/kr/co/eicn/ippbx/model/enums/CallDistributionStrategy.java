package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

import java.util.Objects;

/**
 * 통화분배정책구분
 */
public enum CallDistributionStrategy implements CodeHasable<String> {
    RINGALL("ringall"), LEASTRECENT("leastrecent"), FEWESTCALLS("fewestcalls"),
    RRMEMORY("rrmemory"), RANDOM("random"), SKILL("skill"), CALLRATE("callrate");

    private String code;

    CallDistributionStrategy(String code) {
        this.code = code;
    }

    public static CallDistributionStrategy of(String value) {
        for (CallDistributionStrategy type : CallDistributionStrategy.values()) {
            if (Objects.equals(type.code, value))
                return type;
        }

        return null;
    }

    public String getCode() {
        return code;
    }
}
