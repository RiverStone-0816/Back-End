package kr.co.eicn.ippbx.front.service.api.outbound.voc;

import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.VocGroup;
import kr.co.eicn.ippbx.model.dto.eicn.VOCGroupResponse;
import kr.co.eicn.ippbx.model.form.VOCGroupFormRequest;
import kr.co.eicn.ippbx.model.search.VOCGroupSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class VocGroupApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(VocGroupApiInterface.class);
    private static final String subUrl = "/api/v1/admin/outbound/voc/group/";

    @Value("${eicn.service.servicekind}")
    private String serviceKind;

    public List<VocGroup> list(VOCGroupSearchRequest search) throws IOException, ResultFailException {
        return getList(subUrl, search, VocGroup.class).getData();
    }

    public Pagination<VOCGroupResponse> pagination(VOCGroupSearchRequest search) throws IOException, ResultFailException {
        return getPagination(subUrl + "search", search, VOCGroupResponse.class).getData();
    }

    public VOCGroupResponse get(Integer seq) throws IOException, ResultFailException {
        return getData(subUrl + seq, null, VOCGroupResponse.class).getData();
    }

    public void post(VOCGroupFormRequest form) throws IOException, ResultFailException {
        post(subUrl, form);
    }

    public void put(Integer seq, VOCGroupFormRequest form) throws IOException, ResultFailException {
        put(subUrl + seq, form);
    }

    public void delete(Integer seq) throws IOException, ResultFailException {
        delete(subUrl + seq);
    }

    public List<VocGroup> getArsSmsList(String type) throws IOException, ResultFailException {
        return serviceKind.equals("SC") ? getList(subUrl + type + "/ars-sms-list", null, VocGroup.class).getData() : null;
    }
}
