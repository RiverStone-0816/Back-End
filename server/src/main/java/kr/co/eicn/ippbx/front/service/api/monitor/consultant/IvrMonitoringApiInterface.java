package kr.co.eicn.ippbx.front.service.api.monitor.consultant;

import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.server.model.dto.eicn.MonitorIvrResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
public class IvrMonitoringApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(IvrMonitoringApiInterface.class);
    private static final String subUrl = "/api/v1/admin/monitor/consultant/ivr/";

    public MonitorIvrResponse getIvrList(String serviceNumber) throws IOException, ResultFailException {
        return getData(subUrl, Collections.singletonMap("serviceNumber", serviceNumber), MonitorIvrResponse.class).getData();
    }
}
