package kr.co.eicn.ippbx.front.service.api;

import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.model.dto.eicn.CompanyTreeLevelNameResponse;
import kr.co.eicn.ippbx.model.dto.eicn.OrganizationPersonSummaryResponse;
import kr.co.eicn.ippbx.model.dto.eicn.OrganizationSummaryResponse;
import kr.co.eicn.ippbx.model.entity.eicn.Organization;
import kr.co.eicn.ippbx.model.entity.eicn.OrganizationMeta;
import kr.co.eicn.ippbx.model.form.CompanyTreeNameUpdateFormRequest;
import kr.co.eicn.ippbx.model.form.OrganizationFormRequest;
import kr.co.eicn.ippbx.model.form.OrganizationFormUpdateRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class OrganizationApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(OrganizationApiInterface.class);

    private static final String subUrl = "/api/v1/admin/user/organization/";
    private static final String subUrl2 = "/api/organization/";
    /* 나도 왜 이렇게 api 나눈지 모름.... 이해안됨 */

    public List<Organization> getAllOrganizationPersons() throws IOException, ResultFailException {
        return getList(HttpMethod.GET, subUrl, null, Organization.class).getData();
    }

    public Organization getOrganizationPerson(Integer seq) throws IOException, ResultFailException {
        return getData(HttpMethod.GET, subUrl + seq, null, Organization.class).getData();
    }

    public OrganizationPersonSummaryResponse getOrganizationPersonSummary(Integer seq) throws IOException, ResultFailException {
        return getData(HttpMethod.GET, subUrl + seq + "/summary", null, OrganizationPersonSummaryResponse.class).getData();
    }

    public Integer post(OrganizationFormRequest form) throws IOException, ResultFailException {
        if (StringUtils.isEmpty(form.getParentGroupCode()))
            form.setParentGroupCode(null);
        return getData(HttpMethod.POST, subUrl, form, Integer.class, false).getData();
    }

    public void update(Integer seq, OrganizationFormUpdateRequest form) throws IOException, ResultFailException {
        put(subUrl + seq, form);
    }

    public void delete(Integer seq) throws IOException, ResultFailException {
        delete(subUrl + seq);
    }

    public List<CompanyTreeLevelNameResponse> listMetaType() throws IOException, ResultFailException {
        return getList(HttpMethod.GET, subUrl + "meta-type", null, CompanyTreeLevelNameResponse.class).getData();
    }

    public void updateMetaType(List<CompanyTreeNameUpdateFormRequest> form) throws IOException, ResultFailException {
        put(subUrl + "meta-type", form);
    }

    public List<OrganizationMeta> getMetaTree() throws IOException, ResultFailException {
        return getList(subUrl2 + "meta-tree", null, OrganizationMeta.class).getData();
    }

    public OrganizationSummaryResponse getCompanyTreeGroupCode(String groupCode) throws IOException, ResultFailException {
        return getData(subUrl2 + groupCode, null, OrganizationSummaryResponse.class).getData();
    }

    public List<OrganizationSummaryResponse> getCompanyTreeGroupCodeList(String groupCode) throws IOException, ResultFailException {
        return getList(subUrl2 + groupCode + "/company-tree", null, OrganizationSummaryResponse.class).getData();
    }

    public List<OrganizationSummaryResponse> companyTree(Integer groupLevel, String parentGroupCode) throws IOException, ResultFailException {
        return getList(subUrl2 + "company-tree?groupLevel=" + groupLevel + (StringUtils.isNotEmpty(parentGroupCode) ? "&parentGroupCode=" + parentGroupCode : ""), null, OrganizationSummaryResponse.class).getData();
    }
}
