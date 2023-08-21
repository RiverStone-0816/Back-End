package kr.co.eicn.ippbx.front.service.api.service.etc;

import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.model.dto.eicn.MonitControlResponse;
import kr.co.eicn.ippbx.model.form.MonitControlChangeRequest;
import kr.co.eicn.ippbx.model.search.MonitControlSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class MonitApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(MonitApiInterface.class);

    private static final String subUrl = "/api/v1/admin/service/etc/monit/";

    public List<MonitControlResponse> list(MonitControlSearchRequest search) throws IOException, ResultFailException {
        return getList(subUrl, search, MonitControlResponse.class).getData();
    }

    public List<MonitControlResponse> listDashboard(MonitControlSearchRequest search) throws IOException, ResultFailException {
        return getList(subUrl + "dashboard", search, MonitControlResponse.class).getData();
    }

    public void put(MonitControlChangeRequest form) throws IOException, ResultFailException {
        put(subUrl, form);
    }
}
