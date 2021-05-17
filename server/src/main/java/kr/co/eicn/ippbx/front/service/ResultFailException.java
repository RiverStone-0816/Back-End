package kr.co.eicn.ippbx.front.service;

import kr.co.eicn.ippbx.front.util.JsonResult;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ResultFailException extends Exception {
    private final JsonResult<?> result;

    public ResultFailException(JsonResult<?> result) {
        super(result.getRepresentMessage());
        this.result = result;
    }

    public ResultFailException(String message) {
        super(message);
        result = JsonResult.create(message);
    }
}
