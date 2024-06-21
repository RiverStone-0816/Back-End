package kr.co.eicn.ippbx.front.controller.web.admin.outbound.pds;

import kr.co.eicn.ippbx.util.ReflectionUtils;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.model.search.PdsResultSearch;
import kr.co.eicn.ippbx.front.service.api.application.type.CommonTypeApiInterface;
import kr.co.eicn.ippbx.front.service.api.outbound.pds.PdsGroupApiInterface;
import kr.co.eicn.ippbx.front.service.api.outbound.pds.PdsResultApiInterface;
import kr.co.eicn.ippbx.front.service.excel.PdsResultExcel;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonResultCustomInfo;
import kr.co.eicn.ippbx.meta.jooq.pds.tables.pojos.PdsCustomInfo;
import kr.co.eicn.ippbx.meta.jooq.pds.tables.pojos.ResultCustomInfo;
import kr.co.eicn.ippbx.model.dto.eicn.PDSGroupSummaryResponse;
import kr.co.eicn.ippbx.model.entity.eicn.CommonCodeEntity;
import kr.co.eicn.ippbx.model.entity.eicn.CommonFieldEntity;
import kr.co.eicn.ippbx.model.entity.eicn.CommonTypeEntity;
import kr.co.eicn.ippbx.model.entity.pds.PDSResultCustomInfoEntity;
import kr.co.eicn.ippbx.model.form.PDSResultCustomInfoFormRequest;
import kr.co.eicn.ippbx.model.search.ExecutePDSGroupSearchRequest;
import kr.co.eicn.ippbx.model.search.PDSGroupSearchRequest;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.jooq.tools.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author tinywind
 */
@Slf4j
@AllArgsConstructor
@LoginRequired
@Controller
@RequestMapping("admin/outbound/pds/result")
public class PdsResultController extends BaseController {
    private final PdsResultApiInterface apiInterface;
    private final PdsGroupApiInterface pdsGroupApiInterface;
    private final CommonTypeApiInterface commonTypeApiInterface;

    public static <T extends ResultCustomInfo> Map<Integer, Map<String, Object>> createSeqToFieldNameToValueMap(List<T> list, CommonTypeEntity resultType) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        final Map<Integer, Map<String, Object>> seqToFieldNameToValueMap = new HashMap<>();

        for (val row : list) {
            seqToFieldNameToValueMap.put(row.getSeq(), createFieldNameToValueMap(row, resultType));
        }
        return seqToFieldNameToValueMap;
    }

    public static Map<String, Object> createFieldNameToValueMap(ResultCustomInfo entity, CommonTypeEntity resultType) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        final Map<String, Object> fieldNameToValueMap = new HashMap<>();

        final String removingPrefix = resultType.getKind() + "_";
        for (CommonFieldEntity field : resultType.getFields()) {
            final String fieldName = field.getFieldId().substring(removingPrefix.length()).toLowerCase();

            final String capName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            fieldNameToValueMap.put(field.getFieldId(), CommonResultCustomInfo.class.getDeclaredMethod("getRs" + capName).invoke(entity));
        }

        return fieldNameToValueMap;
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") PdsResultSearch search) {
        val executingPdsList = apiInterface.getExecutingPdsList(new ExecutePDSGroupSearchRequest());
        if (!executingPdsList.isEmpty()) {
            model.addAttribute("executingPdsList", executingPdsList);

            val pdsGroups = pdsGroupApiInterface.list(new PDSGroupSearchRequest());
            if (pdsGroups.isEmpty())
                throw new IllegalArgumentException("PDS 그룹이 존재하지 않습니다.");

            final Map<Integer, String> groups = pdsGroups.stream().collect(Collectors.toMap(PDSGroupSummaryResponse::getSeq, PDSGroupSummaryResponse::getName));
            model.addAttribute("pdsGroups", groups);

            if (search.getExecuteId() == null)
                search.setExecuteId(executingPdsList.get(0).getExecuteId());

            val executePDSGroupEntityOptional = executingPdsList.stream().filter(e -> Objects.equals(e.getExecuteId(), search.getExecuteId())).findFirst();
            if (!executePDSGroupEntityOptional.isPresent())
                throw new IllegalArgumentException("존재하지 않는 PDS실행정보입니다: " + search.getExecuteId());

            val executingPds = executePDSGroupEntityOptional.get();

            final Pagination<PDSResultCustomInfoEntity> pagination = apiInterface.getPagination(search.getExecuteId(), search.convertToRequest("RS_"));
            model.addAttribute("pagination", pagination);

            val pdsGroup = pdsGroupApiInterface.get(executingPds.getPdsGroupId());

            final CommonTypeEntity pdsType = commonTypeApiInterface.get(pdsGroup.getPdsType());
            model.addAttribute("pdsType", pdsType);
            final CommonTypeEntity resultType = commonTypeApiInterface.get(pdsGroup.getResultType());
            model.addAttribute("resultType", resultType);

            final Map<String, Map<String, String>> codeMap = new HashMap<>();
            pdsType.getFields().stream()
                    .filter(e -> e.getCodes() != null && e.getCodes().size() > 0)
                    .forEach(e -> {
                        final Map<String, String> codes = e.getCodes().stream().collect(Collectors.toMap(CommonCodeEntity::getCodeId, CommonCodeEntity::getCodeName));
                        codeMap.put(e.getFieldId(), codes);
                    });
            resultType.getFields().stream()
                    .filter(e -> e.getCodes() != null && e.getCodes().size() > 0)
                    .forEach(e -> {
                        final Map<String, String> codes = e.getCodes().stream().collect(Collectors.toMap(CommonCodeEntity::getCodeId, CommonCodeEntity::getCodeName));
                        codeMap.put(e.getFieldId(), codes);
                    });
            model.addAttribute("codeMap", new JSONObject(codeMap));

            model.addAttribute("customIdToFieldNameToValueMap", PdsCustominfoController.createCustomIdToFieldNameToValueMap(pagination.getRows().stream().map(PDSResultCustomInfoEntity::getPdsCustomInfoEntity).collect(Collectors.toList()), pdsType));
            model.addAttribute("seqToFieldNameToValueMap", createSeqToFieldNameToValueMap(pagination.getRows(), resultType));
        }
        // FIXME: 상담원 어덯게 추출??

        return "admin/outbound/pds/result/ground";
    }

    @SneakyThrows
    @GetMapping("{executeId}/{seq}/modal")
    public String modal(Model model, @PathVariable String executeId, @PathVariable Integer seq, @ModelAttribute("form") PDSResultCustomInfoFormRequest form) {
        val entity = apiInterface.get(executeId, seq);
        model.addAttribute("entity", entity);

        val pdsGroup = pdsGroupApiInterface.get(entity.getGroupId());
        model.addAttribute("pdsGroup", pdsGroup);

        final CommonTypeEntity resultType = commonTypeApiInterface.get(pdsGroup.getResultType());
        model.addAttribute("resultType", resultType);

        final Map<String, Object> fieldNameToValueMap = createFieldNameToValueMap(entity, resultType);
        model.addAttribute("fieldNameToValueMap", fieldNameToValueMap);

        ReflectionUtils.copy(form, entity);

        final Map<String, String> fieldToRelatedField = new HashMap<>();
        for (CommonFieldEntity field : resultType.getFields()) {
            if (field.getCodes() != null) {
                for (CommonCodeEntity code : field.getCodes()) {
                    if (StringUtils.isNotEmpty(code.getRelatedFieldId())) {
                        fieldToRelatedField.put(field.getFieldId(), code.getRelatedFieldId());
                    }
                }
            }
        }
        model.addAttribute("fieldToRelatedField", fieldToRelatedField);

        return "admin/outbound/pds/result/modal";
    }

    @SneakyThrows
    @GetMapping("_excel")
    public void downloadExcel(PdsResultSearch search, HttpServletResponse response) {
        val executingPdsList = apiInterface.getExecutingPdsList(new ExecutePDSGroupSearchRequest());

        if (search.getExecuteId() == null) {
            if (executingPdsList.isEmpty())
                throw new IllegalArgumentException("PDS실행정보가 존재하지 않습니다.");

            search.setExecuteId(executingPdsList.get(0).getExecuteId());
        }

        val executePDSGroupOptional = executingPdsList.stream().filter(e -> Objects.equals(search.getExecuteId(), e.getExecuteId())).findFirst();
        if (!executePDSGroupOptional.isPresent())
            search.setExecuteId(executingPdsList.get(0).getExecuteId());

        val executePDSGroup = executingPdsList.stream().filter(e -> Objects.equals(search.getExecuteId(), e.getExecuteId())).findFirst().get();

        search.setPage(1);
        search.setLimit(10000);
        val pagination = apiInterface.getPagination(search.getExecuteId(), search.convertToRequest("RS_"));

        val pdsGroup = pdsGroupApiInterface.get(executePDSGroup.getPdsGroupId());
        final CommonTypeEntity pdsType = commonTypeApiInterface.get(pdsGroup.getPdsType());
        final CommonTypeEntity resultType = commonTypeApiInterface.get(pdsGroup.getResultType());

        new PdsResultExcel(pagination.getRows(), pdsType, resultType).generator(response, "PDS 상담이력");
    }
}
