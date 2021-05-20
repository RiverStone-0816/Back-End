package kr.co.eicn.ippbx.front.service.api.dashboard;

import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.model.dto.eicn.*;
import kr.co.eicn.ippbx.model.form.DashboardViewListFormRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
@PropertySource("classpath:application.properties")
public class DashboardApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(DashboardApiInterface.class);
    private static final String subUrl = "/api/v1/admin/dashboard/";

    @Value("${server.servicekind}")
    private String serviceKind;

    public DashHuntMonitorResponse getCustomWaitMonitor() throws IOException, ResultFailException {
        return getData(subUrl + "custom-wait-monitor", null, DashHuntMonitorResponse.class).getData();
    }

    public DashAveByServiceResponse getAveByService() throws IOException, ResultFailException {
        return getData(subUrl + "ave-by-service", null, DashAveByServiceResponse.class).getData();
    }

    public DashMonitorByHuntResponse getMonitorByHunt() throws IOException, ResultFailException {
        return getData(subUrl + "monitor-by-hunt", null, DashMonitorByHuntResponse.class).getData();
    }

    public List<ExcellentConsultant> getExcellentCS() throws IOException, ResultFailException {
        return getList(subUrl + "excellent-cs", null, ExcellentConsultant.class).getData();
    }

    public DashTotalStatResponse getTotalStat() throws IOException, ResultFailException {
        return getData(subUrl + "total-stat", null, DashTotalStatResponse.class).getData();
    }

    public DashServiceStatResponse getServiceStat(String number) throws IOException, ResultFailException {
        return getData(subUrl + "service-stat/" + number, null, DashServiceStatResponse.class).getData();
    }

    public DashHuntMonitorResponse getTotalMonitor() throws IOException, ResultFailException {
        return getData(subUrl + "total-monitor", null, DashHuntMonitorResponse.class).getData();
    }

    public DashHuntMonitorResponse getHuntMonitor(String number) throws IOException, ResultFailException {
        return getData(subUrl + "hunt-monitor/" + number, null, DashHuntMonitorResponse.class).getData();
    }

    public DashCurrentCustomWaitResponse getCurrentCustomWait() throws IOException, ResultFailException {
        return getData(subUrl + "current-custom-wait", null, DashCurrentCustomWaitResponse.class).getData();
    }

    public DashHuntMonitorResponse getCurrentResult() throws IOException, ResultFailException {
        return getData(subUrl + "current-result", null, DashHuntMonitorResponse.class).getData();
    }

    public DashCurrentResultCallResponse getCurrentResultCall() throws IOException, ResultFailException {
        return getData(subUrl + "current-result-call", null, DashCurrentResultCallResponse.class).getData();
    }

    public DashServerMonitorResponse getServerMonitor(String host) throws IOException, ResultFailException {
        return getData(subUrl + "server-monitor/" + host, null, DashServerMonitorResponse.class).getData();
    }

    public List<DashListResponse> getDashboardList() throws IOException, ResultFailException {
        return getList(subUrl + "dashboard-list", Collections.singletonMap("serviceKind", serviceKind), DashListResponse.class).getData();
    }

    public List<DashViewListResponse> getDashboardViewList() throws IOException, ResultFailException {
        return getList(subUrl + "dashboard-view-list", null, DashViewListResponse.class).getData();
    }

    public void post(DashboardViewListFormRequest form) throws IOException, ResultFailException {
        post(subUrl, form);
    }

    public void put(Integer seq, DashboardViewListFormRequest form) throws IOException, ResultFailException {
        put(subUrl + seq, form);
    }

    public void delete(Integer seq) throws IOException, ResultFailException {
        delete(subUrl + seq);
    }

}
