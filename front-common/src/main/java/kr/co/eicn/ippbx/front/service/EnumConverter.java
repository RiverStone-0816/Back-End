package kr.co.eicn.ippbx.front.service;

import kr.co.eicn.ippbx.front.config.Constants;
import kr.co.eicn.ippbx.util.spring.RequestMessage;
import kr.co.eicn.ippbx.util.ClassUtils;
import kr.co.eicn.ippbx.util.CodeHasable;
import kr.co.eicn.ippbx.util.EnumUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class EnumConverter extends BaseService {
    private static final Logger logger = LoggerFactory.getLogger(EnumConverter.class);

    private final Map<String, Class<Enum<?>>> enumClassNameToEnumClass;

    @Autowired
    private RequestMessage message;

    @SuppressWarnings("unchecked")
    public EnumConverter() throws IOException, ClassNotFoundException {
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
        return value != null ? message.getEnumText(value) : null;
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
