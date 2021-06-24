package kr.co.eicn.ippbx.front.controller.api.stat;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.service.api.stat.ConsultantStatApiInterface;
import kr.co.eicn.ippbx.model.dto.statdb.StatMyCallByTimeResponse;
import kr.co.eicn.ippbx.model.dto.statdb.StatMyCallResponse;
import kr.co.eicn.ippbx.model.dto.statdb.StatUserResponse;
import kr.co.eicn.ippbx.model.search.StatUserSearchRequest;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author tinywind
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(value = "api/stat", produces = MediaType.APPLICATION_JSON_VALUE)
public class StatApiController extends BaseController {

    private final ConsultantStatApiInterface apiInterface;

    @SneakyThrows
    @GetMapping("user")
    public List<StatUserResponse<?>> list(StatUserSearchRequest search) {
        return apiInterface.list(search);
    }

    @SneakyThrows
    @GetMapping("user/total")
    public StatUserResponse.UserStat getTotal(StatUserSearchRequest search) {
        return apiInterface.getTotal(search);
    }

    @SneakyThrows
    @GetMapping("user/my-call-stat")
    public StatMyCallResponse myCallStat() {
        return apiInterface.myCallStat();
    }

    @SneakyThrows
    @GetMapping("user/my-call-time")
    public StatMyCallByTimeResponse myCallByTimeStat() {
        return apiInterface.myCallByTimeStat();
    }
}
