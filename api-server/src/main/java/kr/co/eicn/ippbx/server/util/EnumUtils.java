package kr.co.eicn.ippbx.server.util;

import java.util.Objects;

public class EnumUtils {
    public static <E extends Enum<E> & CodeHasable<C>, C> E of(Class<E> enumClass, C code) {
        for (E e : enumClass.getEnumConstants()) {
            if (Objects.equals(e.getCode(), code))
                return e;
        }
        return null;
    }
}
