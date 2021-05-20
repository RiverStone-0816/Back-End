package kr.co.eicn.ippbx.front.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.eicn.ippbx.exception.ValidationException;
import kr.co.eicn.ippbx.util.JsonResult;
import kr.co.eicn.ippbx.util.spring.BaseForm;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Objects;

@AllArgsConstructor
@Slf4j
@Aspect
@Component
public class ApiControllerAspect {
    private final ObjectMapper objectMapper;

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping) && execution(public * kr.co.eicn.ippbx..api..*.*(..))")
    public void postMapping() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PutMapping) && execution(public * kr.co.eicn.ippbx..api..*.*(..))")
    public void putMapping() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PatchMapping) && execution(public * kr.co.eicn.ippbx..api..*.*(..))")
    public void patchMapping() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping) && execution(public * kr.co.eicn.ippbx..api..*.*(..))")
    public void getMapping() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.DeleteMapping) && execution(public * kr.co.eicn.ippbx..api..*.*(..))")
    public void deleteMapping() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping) && execution(public * kr.co.eicn.ippbx..api..*.*(..))")
    public void requestMapping() {
    }

    @AfterReturning(value = "postMapping() || putMapping() || patchMapping() || getMapping() || deleteMapping() || requestMapping()", returning = "returnValue")
    public void writeVoidJsonResult(JoinPoint joinPoint, Object returnValue) throws IOException {
        if (Objects.equals(joinPoint.getSignature().getDeclaringType(), ApiExceptionHandler.class))
            return;

        if (returnValue == null) {
            final HttpServletResponse response = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getResponse();
            Objects.requireNonNull(response).getWriter().print(objectMapper.writeValueAsString(JsonResult.create()));
        }
    }

    @Before("postMapping() || putMapping() || patchMapping()")
    public void validateFormData(JoinPoint joinPoint) {
        final Annotation[][] parameterAnnotations = ((MethodSignature) joinPoint.getSignature()).getMethod().getParameterAnnotations();

        final Integer validIndex = getFormIndex(parameterAnnotations);
        if (validIndex == null)
            return;

        final Object body = joinPoint.getArgs()[validIndex];
        final BindingResult bindingResult = (BindingResult) joinPoint.getArgs()[validIndex + 1];

        if (body instanceof BaseForm) {
            ((BaseForm) body).validate(bindingResult);
        } else if (body.getClass().isArray()) {
            final Object[] formArray = (Object[]) body;
            if (formArray.length == 0) {
                log.warn("RequestBody is null: " + joinPoint);
                return;
            }

            for (Object o : formArray) {
                if (!(o instanceof BaseForm)) {
                    log.warn("can't find BaseForm: " + joinPoint);
                    continue;
                }
                ((BaseForm) o).validate(bindingResult);
            }
        } else if (body instanceof Iterable) {
            final Iterable<?> iter = (Iterable<?>) body;
            if (!iter.iterator().hasNext()) {
                log.warn("RequestBody is null: " + joinPoint);
                return;
            }

            for (Object o : iter) {
                if (!(o instanceof BaseForm)) {
                    log.warn("can't find BaseForm: " + joinPoint);
                    continue;
                }
                ((BaseForm) o).validate(bindingResult);
            }
        }

        if (bindingResult.hasErrors())
            throw new ValidationException(bindingResult);
    }

    private Integer getFormIndex(Annotation[][] parameterAnnotations) {
        for (int i = 0; i < parameterAnnotations.length; i++) {
            final Annotation[] annotations = parameterAnnotations[i];
            for (Annotation annotation : annotations) {
                if (annotation instanceof Valid)
                    return i;
            }
        }

        return null;
    }
}
