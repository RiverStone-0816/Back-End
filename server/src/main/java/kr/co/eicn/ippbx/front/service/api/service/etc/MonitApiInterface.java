package kr.co.eicn.ippbx.front.service.api.service.etc;

import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.front.util.page.Pagination;
import kr.co.eicn.ippbx.server.exception.ValidationException;
import kr.co.eicn.ippbx.server.model.dto.eicn.CidInfoResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.MonitControlResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.PersonListSummary;
import kr.co.eicn.ippbx.server.model.form.CidInfoChangeRequest;
import kr.co.eicn.ippbx.server.model.form.CidInfoUpdateFormRequest;
import kr.co.eicn.ippbx.server.model.form.MonitControlChangeRequest;
import kr.co.eicn.ippbx.server.model.search.CidInfoSearchRequest;
import kr.co.eicn.ippbx.server.model.search.MonitControlSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MonitApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(MonitApiInterface.class);

    private static final String subUrl = "/api/v1/admin/service/etc/monit/";

    public List<MonitControlResponse> list(MonitControlSearchRequest search) throws IOException, ResultFailException {
        return getList(subUrl, search, MonitControlResponse.class).getData();
    }

    public void put(MonitControlChangeRequest form) throws IOException, ResultFailException {
        put(subUrl, form);
    }
}
