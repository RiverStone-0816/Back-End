package kr.co.eicn.ippbx.front.service;

import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.model.dto.eicn.LguCallBotInfoResponse;
import kr.co.eicn.ippbx.util.ResultFailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class LguCallBotApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(LguCallBotApiInterface.class);

    public List<LguCallBotInfoResponse> getCallBotApiUrl() throws IOException, ResultFailException {
        return getList("/api/call-bot", null, LguCallBotInfoResponse.class).getData();
    }
}
