package kr.co.eicn.ippbx.front.service.api.user.tel;

import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.front.util.UrlUtils;
import kr.co.eicn.ippbx.server.model.dto.eicn.NumberSummaryResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.SummaryNumber070Response;
import kr.co.eicn.ippbx.server.model.enums.NumberType;
import kr.co.eicn.ippbx.server.model.form.NumberTypeChangeRequest;
import kr.co.eicn.ippbx.server.model.search.NumberSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class NumberApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(NumberApiInterface.class);

    private static final String subUrl = "/api/v1/admin/user/tel/number/";

    public List<NumberSummaryResponse> list(NumberSearchRequest search) throws IOException, ResultFailException {
        return getList(subUrl, search, NumberSummaryResponse.class).getData();
    }

    public void changeType(String number, NumberType type) throws IOException, ResultFailException {
        final NumberTypeChangeRequest req = new NumberTypeChangeRequest();
        req.setType(type.getCode());

        patch(subUrl + number + "/type", req);
    }

    public List<SummaryNumber070Response> typeNumbers(Byte type, String host) throws IOException, ResultFailException {
        return getList(subUrl + "type-numbers", UrlUtils.makeParamMap("type", type != null ? "" + type : "", "host", host), SummaryNumber070Response.class).getData();
    }
}
