package kr.co.eicn.ippbx.front.service.api.monitor.consultant;

import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.model.dto.eicn.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class QueueMonitoringApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(QueueMonitoringApiInterface.class);
    private static final String subUrl = "/api/v1/admin/monitor/consultant/queue/";

    public List<MonitorQueueSummaryResponse> getSummary() throws IOException, ResultFailException {
        return getList(subUrl + "summary", null, MonitorQueueSummaryResponse.class).getData();
    }

    public List<MonitorQueueSummaryPerson> getPersonStatus() throws IOException, ResultFailException {
        return getList(subUrl + "person-status", null, MonitorQueueSummaryPerson.class).getData();
    }

    public MonitorQueueTotalResponse getStatSummary() throws IOException, ResultFailException {
        return getData(subUrl + "stat-summary", null, MonitorQueueTotalResponse.class).getData();
    }
}
