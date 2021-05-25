package kr.co.eicn.ippbx.front.service.api;

import com.fasterxml.jackson.databind.JavaType;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.util.JsonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class DaemonInfoInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(AuthApiInterface.class);

    @SuppressWarnings("unchecked")
    public Map<String, String> getSocketList() throws IOException, ResultFailException {
        final JavaType type = typeFactory.constructParametricType(Map.class, String.class, String.class);
        return (Map<String, String>) getData("/api/daemon", null, typeFactory.constructParametricType(JsonResult.class, type)).getData();
    }
}
