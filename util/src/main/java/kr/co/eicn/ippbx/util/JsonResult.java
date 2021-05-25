package kr.co.eicn.ippbx.util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.ToString;
import org.springframework.validation.BindingResult;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author tinywind
 * @since 2018-01-13
 */
@ToString
public class JsonResult<T> implements Serializable {
    private Result result;
    private String reason;
    private T data;
    private List<FieldError> fieldErrors;

    public JsonResult() {
        this(Result.success);
    }

    public JsonResult(Result result) {
        this(result, null);
    }

    public JsonResult(Result result, String reason) {
        this(result, reason, null);
    }

    public JsonResult(Exception e) {
        this(Result.failure, e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
    }

    public JsonResult(BindingResult bindingResult) {
        this(bindingResult.hasErrors() ? Result.failure : Result.success, null, bindingResult);
    }

    public JsonResult(Result result, String reason, BindingResult bindingResult) {
        setFieldErrors(new ArrayList<>());
        setResult(result);
        setReason(reason);
        if (bindingResult != null)
            setBindingResult(bindingResult);
    }

    public static <T> JsonResult<T> data(T data) {
        final JsonResult<T> result = new JsonResult<>();
        result.setData(data);
        return result;
    }

    public static <T> JsonResult<T> create() {
        return new JsonResult<>();
    }

    public static <T> JsonResult<T> create(Result result) {
        return new JsonResult<>(result);
    }

    public static <T> JsonResult<T> create(Result result, String reason) {
        return new JsonResult<>(result, reason);
    }

    public static <T> JsonResult<T> create(Result result, String reason, BindingResult bindingResult) {
        return new JsonResult<>(result, reason, bindingResult);
    }

    public static <T> JsonResult<T> create(Exception e) {
        return new JsonResult<>(e);
    }

    public static <T> JsonResult<T> create(BindingResult bindingResult) {
        return new JsonResult<>(bindingResult);
    }

    public static <T> JsonResult<T> create(String reason) {
        return new JsonResult<>(Result.failure, reason);
    }

    public void setBindingResult(BindingResult bindingResult) {
        setResult(bindingResult.hasErrors() ? Result.failure : Result.success);

        List<FieldError> fieldErrors = getFieldErrors();
        if (fieldErrors == null)
            fieldErrors = new ArrayList<>();

        for (org.springframework.validation.FieldError error : bindingResult.getFieldErrors())
            fieldErrors.add(new FieldError(error.getField(), error.isBindingFailure(), error.getDefaultMessage()));

        setFieldErrors(fieldErrors);
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public List<FieldError> getFieldErrors() {
        return fieldErrors;
    }

    public void setFieldErrors(List<FieldError> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }

    public String getRepresentMessage() {
        if (reason != null)
            return reason;

        if (fieldErrors != null && fieldErrors.size() > 0)
            return fieldErrors.get(0).getDefaultMessage();

        return null;
    }

    public void setRepresentMessage(String representMessage) {
    }

    @JsonIgnore
    public boolean isSuccess() {
        return Objects.equals(result, Result.success);
    }

    @JsonIgnore
    public boolean isFailure() {
        return !isSuccess();
    }

    public enum Result {
        success, failure
    }

    public static class FieldError {
        private String defaultMessage;
        private String field;
        private Boolean typeMatchError;

        public FieldError(String field, Boolean typeMatchError, String defaultMessage) {
            this.defaultMessage = defaultMessage;
            this.field = field;
            this.typeMatchError = typeMatchError;
        }

        public String getDefaultMessage() {
            return defaultMessage;
        }

        public void setDefaultMessage(String defaultMessage) {
            this.defaultMessage = defaultMessage;
        }

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public Boolean getTypeMatchError() {
            return typeMatchError;
        }

        public void setTypeMatchError(Boolean typeMatchError) {
            this.typeMatchError = typeMatchError;
        }
    }
}
