package kr.co.eicn.ippbx.front.controller.api.user;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.OrganizationApiInterface;
import kr.co.eicn.ippbx.front.util.spring.BaseForm;
import kr.co.eicn.ippbx.server.model.dto.eicn.CompanyTreeLevelNameResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.OrganizationPersonSummaryResponse;
import kr.co.eicn.ippbx.server.model.entity.eicn.Organization;
import kr.co.eicn.ippbx.server.model.form.CompanyTreeNameUpdateFormRequest;
import kr.co.eicn.ippbx.server.model.form.OrganizationFormRequest;
import kr.co.eicn.ippbx.server.model.form.OrganizationFormUpdateRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author tinywind
 */
@Api(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequestMapping(value = "api/organization", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrganizationApiController extends ApiBaseController {
    private static final Logger logger = LoggerFactory.getLogger(OrganizationApiController.class);

    @Autowired
    private OrganizationApiInterface organizationApiInterface;

    @GetMapping("")
    public List<Organization> getAllOrganizationPersons() throws IOException, ResultFailException {
        return organizationApiInterface.getAllOrganizationPersons();
    }

    @GetMapping("{seq}")
    public Organization getOrganizationPerson(@PathVariable Integer seq) throws IOException, ResultFailException {
        return organizationApiInterface.getOrganizationPerson(seq);
    }

    /**
     * 단일 조직구성의 설명: 계층 구조상의 전체 부모 구성원들 정보와 자식 구성원 및 사용자들의 숫자를 카운팅한다.
     */
    @GetMapping("/{seq}/summary")
    public OrganizationPersonSummaryResponse getOrganizationPersonSummary(@PathVariable Integer seq) throws IOException, ResultFailException {
        return organizationApiInterface.getOrganizationPersonSummary(seq);
    }

    /**
     * 조직구성 등록
     */
    @PostMapping("")
    public Integer post(@Valid @RequestBody OrganizationFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        return organizationApiInterface.post(form);
    }

    /**
     * 조직구성 수정
     */
    @PutMapping("{seq}")
    public void update(@Valid @RequestBody OrganizationFormUpdateRequest form, BindingResult bindingResult, @PathVariable Integer seq) throws IOException, ResultFailException {
        organizationApiInterface.update(seq, form);
    }

    /**
     * 조직구성 삭제
     */
    @DeleteMapping("{seq}")
    public void delete(@PathVariable Integer seq) throws IOException, ResultFailException {
        organizationApiInterface.delete(seq);
    }

    @GetMapping("meta-type")
    public List<CompanyTreeLevelNameResponse> listMetaType() throws IOException, ResultFailException {
        return organizationApiInterface.listMetaType();
    }

    @PutMapping("meta-type")
    public void updateMetaType(@Valid @RequestBody CompanyTreeNameUpdateForm form, BindingResult bindingResult) throws IOException, ResultFailException {
        final List<CompanyTreeNameUpdateFormRequest> request = new ArrayList<>();
        form.getTreeNameMap().forEach((level, name) -> {
            final CompanyTreeNameUpdateFormRequest e = new CompanyTreeNameUpdateFormRequest();
            e.setGroupLevel(level);
            e.setGroupTreeName(name);
            request.add(e);
        });

        organizationApiInterface.updateMetaType(request);
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class CompanyTreeNameUpdateForm extends BaseForm {
        private Map<Integer, String> treeNameMap;
    }
}
