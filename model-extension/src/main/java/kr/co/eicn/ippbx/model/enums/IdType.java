package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;
import org.springframework.security.core.GrantedAuthority;

import java.util.Objects;

/**
 * 유저 권한 타입
 */
public enum IdType implements CodeHasable<String>, GrantedAuthority {
    MASTER("J"), SUPER_ADMIN("A"), ADMIN("B"), USER("M");

    private String code;

    IdType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String getAuthority() {
        return "ROLE_" + this.name();
    }

    public static IdType of (String value) {
        for (IdType type : IdType.values()){
            if (type.getCode().equals(value))
                return type;
        }

        return null;
    }

    public static boolean isAdmin(String idType) {
        return Objects.equals(MASTER, of(idType)) || Objects.equals(SUPER_ADMIN, of(idType));
    }

    public static boolean isConsultant(String idType) {
        return Objects.equals(USER, of(idType));
    }
}
