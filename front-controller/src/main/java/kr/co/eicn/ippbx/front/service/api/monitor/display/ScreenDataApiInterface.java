package kr.co.eicn.ippbx.front.service.api.monitor.display;

import kr.co.eicn.ippbx.front.model.ScreenDataForByHunt;
import kr.co.eicn.ippbx.front.model.ScreenDataForByService;
import kr.co.eicn.ippbx.front.model.ScreenDataForIntegration;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ScreenDataApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(ScreenDataApiInterface.class);
    private static final String subUrl = "/api/v1/admin/monitor/screen-data/";

    public ScreenDataForIntegration integration() throws IOException, ResultFailException {
        return getData(subUrl + "?expressionType=INTEGRATION", null, ScreenDataForIntegration.class).getData();
    }

    public ScreenDataForByHunt byHunt() throws IOException, ResultFailException {
        return getData(subUrl + "?expressionType=BY_HUNT", null, ScreenDataForByHunt.class).getData();
    }

    public ScreenDataForByService byService() throws IOException, ResultFailException {
        return getData(subUrl + "?expressionType=BY_SERVICE", null, ScreenDataForByService.class).getData();
    }
}
