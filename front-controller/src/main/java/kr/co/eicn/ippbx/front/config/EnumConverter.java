package kr.co.eicn.ippbx.front.config;

import kr.co.eicn.ippbx.util.ClassUtils;
import kr.co.eicn.ippbx.util.CodeHasable;
import kr.co.eicn.ippbx.util.EnumUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class EnumConverter {
    public static final EnumConverter instance;

    static {
        EnumConverter converter = null;
        try {
            converter = new EnumConverter();
        } catch (IOException | ClassNotFoundException | URISyntaxException e) {
            log.error(e.getMessage(), e);
        }
        instance = converter;
    }

    private final Map<String, Class<Enum<?>>> enumClassNameToEnumClass;

    @SuppressWarnings("unchecked")
    private EnumConverter() throws IOException, ClassNotFoundException, URISyntaxException {
        final Map<String, Class<Enum<?>>> map = new HashMap<>();

        for (String packageName : Constants.BASE_MODEL_PACKAGE) {
            ClassUtils.getClasses(packageName).stream()
                    .filter(e -> !e.isInterface() && e.isEnum())
                    .forEach(e -> {
                        map.put(e.getName(), (Class<Enum<?>>) e);
                        map.put(e.getSimpleName(), (Class<Enum<?>>) e);
                    });
        }

        enumClassNameToEnumClass = Collections.unmodifiableMap(map);
    }

    public <E extends Enum<E> & CodeHasable<C>, C> String messageOf(String enumClassName, C code) {
        final E value = of(enumClassName, code);
        return value != null ? ApplicationBeanAware.requestMessage().getEnumText(value) : null;
    }

    @SuppressWarnings("unchecked")
    public <E extends Enum<E> & CodeHasable<C>, C> E of(String enumClassName, C code) {
        final Class<Enum<?>> enumClass = enumClassNameToEnumClass.get(enumClassName);
        if (enumClass == null)
            return null;

        if (ClassUtils.isExpands(enumClass, CodeHasable.class))
            return EnumUtils.of((Class<E>) (Class<?>) enumClass, code);

        for (Enum<?> enumConstant : enumClass.getEnumConstants())
            if (Objects.equals(enumConstant.name(), code) || Objects.equals(enumConstant, code))
                return (E) enumConstant;

        return null;
    }
}
