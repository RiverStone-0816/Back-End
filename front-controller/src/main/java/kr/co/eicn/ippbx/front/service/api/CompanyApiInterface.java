package kr.co.eicn.ippbx.front.service.api;

import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CompanyInfo;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ServerInfo;
import kr.co.eicn.ippbx.model.dto.eicn.SummaryCompanyServerResponse;
import kr.co.eicn.ippbx.model.entity.eicn.CmpMemberStatusCodeEntity;
import kr.co.eicn.ippbx.model.entity.eicn.CompanyEntity;
import kr.co.eicn.ippbx.model.entity.eicn.CompanyLicenceEntity;
import org.apache.commons.collections4.map.SingletonMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
public class CompanyApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(CompanyApiInterface.class);

    private static final String subUrl = "/api/company/";

    public CompanyLicenceEntity getLicense() throws IOException, ResultFailException {
        return getData(subUrl + "licence", null, CompanyLicenceEntity.class).getData();
    }

    public CompanyEntity getInfo() throws IOException, ResultFailException {
        return getData(subUrl + "info", null, CompanyEntity.class).getData();
    }

    public CompanyInfo getInfo(String companyId) throws IOException, ResultFailException {
        return getData(subUrl + "info", Collections.singletonMap("companyId", companyId), CompanyInfo.class).getData();
    }

    public List<CmpMemberStatusCodeEntity> getMemberStatusCodes() throws IOException, ResultFailException {
        return getList(subUrl + "member-status-codes", null, CmpMemberStatusCodeEntity.class).getData();
    }

    public List<SummaryCompanyServerResponse> getPBXServers() throws IOException, ResultFailException {
        return getList(subUrl + "add-server", null, SummaryCompanyServerResponse.class).getData();
    }

    public ServerInfo pbxServerInfo() throws IOException, ResultFailException {
        return getData(subUrl + "pbx-server", null, ServerInfo.class).getData();
    }

    public boolean checkService(String service) throws IOException, ResultFailException {
        return getData(subUrl + "check-service", Collections.singletonMap("service", service), Boolean.class).getData();
    }

    public String getServices() throws IOException, ResultFailException {
        return getData(subUrl + "get-service", null, String.class).getData();
    }
}
