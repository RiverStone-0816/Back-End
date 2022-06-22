package kr.co.eicn.ippbx.front.controller.web.admin.application.maindb;

import kr.co.eicn.ippbx.front.service.api.SearchApiInterface;
import kr.co.eicn.ippbx.util.ReflectionUtils;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.model.search.MaindbResultSearch;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.application.maindb.MaindbDataApiInterface;
import kr.co.eicn.ippbx.front.service.api.application.maindb.MaindbGroupApiInterface;
import kr.co.eicn.ippbx.front.service.api.application.maindb.MaindbResultApiInterface;
import kr.co.eicn.ippbx.front.service.api.application.type.CommonTypeApiInterface;
import kr.co.eicn.ippbx.front.service.api.user.user.UserApiInterface;
import kr.co.eicn.ippbx.front.service.excel.MaindbResultExcel;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonResultCustomInfo;
import kr.co.eicn.ippbx.model.RecordFile;
import kr.co.eicn.ippbx.model.dto.eicn.MaindbGroupDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.PersonSummaryResponse;
import kr.co.eicn.ippbx.model.dto.eicn.search.SearchMaindbGroupResponse;
import kr.co.eicn.ippbx.model.entity.customdb.MaindbCustomInfoEntity;
import kr.co.eicn.ippbx.model.entity.customdb.ResultCustomInfoEntity;
import kr.co.eicn.ippbx.model.entity.eicn.CommonCodeEntity;
import kr.co.eicn.ippbx.model.entity.eicn.CommonFieldEntity;
import kr.co.eicn.ippbx.model.entity.eicn.CommonTypeEntity;
import kr.co.eicn.ippbx.model.form.ResultCustomInfoFormRequest;
import kr.co.eicn.ippbx.model.search.PersonSearchRequest;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jooq.tools.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author tinywind
 */
@AllArgsConstructor
@LoginRequired
@Controller
@RequestMapping("admin/application/maindb/result")
public class MaindbResultController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(MaindbResultController.class);

    private final MaindbResultApiInterface apiInterface;
    private final MaindbGroupApiInterface maindbGroupApiInterface;
    private final MaindbDataApiInterface maindbDataApiInterface;
    private final CommonTypeApiInterface commonTypeApiInterface;
    private final SearchApiInterface searchApiInterface;

    public static Map<Integer, Map<String, Object>> createSeqToFieldNameToValueMap(List<CommonResultCustomInfo> list, CommonTypeEntity resultType) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        final Map<Integer, Map<String, Object>> seqToFieldNameToValueMap = new HashMap<>();

        for (CommonResultCustomInfo row : list) {
            seqToFieldNameToValueMap.put(row.getSeq(), createFieldNameToValueMap(row, resultType));
        }
        return seqToFieldNameToValueMap;
    }

    public static LinkedHashMap<String, Object> createFieldNameToValueMap(CommonResultCustomInfo entity, CommonTypeEntity resultType) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        final LinkedHashMap<String, Object> fieldNameToValueMap = new LinkedHashMap<>();

        final String removingPrefix = resultType.getKind() + "_";
        for (CommonFieldEntity field : resultType.getFields()) {
            final String fieldName = field.getFieldId().substring(removingPrefix.length()).toLowerCase();

            final String capName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            fieldNameToValueMap.put(field.getFieldId(), CommonResultCustomInfo.class.getDeclaredMethod("getRs" + capName).invoke(entity));
        }

        return fieldNameToValueMap;
    }

    @SuppressWarnings("unchecked")
    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") MaindbResultSearch search) throws IOException, ResultFailException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final List<SearchMaindbGroupResponse> customdbGroups = apiInterface.customdbGroup();
        if (customdbGroups.size() != 0) {

            final Map<Integer, String> groups = customdbGroups.stream().collect(Collectors.toMap(SearchMaindbGroupResponse::getSeq, SearchMaindbGroupResponse::getName));
            model.addAttribute("customdbGroups", groups);

            if (search.getGroupSeq() == null)
                search.setGroupSeq(customdbGroups.get(0).getSeq());

            final Pagination<ResultCustomInfoEntity> pagination = apiInterface.getPagination(search.getGroupSeq(), search.convertToRequest(""));
            model.addAttribute("pagination", pagination);

            final MaindbGroupDetailResponse customGroup = maindbGroupApiInterface.get(search.getGroupSeq());
            final CommonTypeEntity customDbType = commonTypeApiInterface.get(customGroup.getMaindbType());
            customDbType.setFields(customDbType.getFields().stream().filter(e -> "Y".equals(e.getIsdisplayList())).collect(Collectors.toList()));
            model.addAttribute("customDbType", customDbType);
            final CommonTypeEntity resultType = commonTypeApiInterface.get(customGroup.getResultType());
            resultType.setFields(resultType.getFields().stream().filter(e -> "Y".equals(e.getIsdisplayList())).collect(Collectors.toList()));
            model.addAttribute("resultType", resultType);

            final Map<String, Map<String, String>> codeMap = new HashMap<>();
            customDbType.getFields().stream()
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

            model.addAttribute("seqToFieldNameToValueMap", createSeqToFieldNameToValueMap((List<CommonResultCustomInfo>) (List<?>) pagination.getRows(), resultType));
            model.addAttribute("customIdToFieldNameToValueMap", MaindbDataController.createCustomIdToFieldNameToValueMap(pagination.getRows().stream().map(ResultCustomInfoEntity::getCustomInfo).collect(Collectors.toList()), customDbType));

            model.addAttribute("users", searchApiInterface.persons());
        }
        return "admin/application/maindb/result/ground";
    }

    @GetMapping("{seq}/modal")
    public String modal(Model model, @PathVariable Integer seq, @ModelAttribute("form") ResultCustomInfoFormRequest form) throws IOException, ResultFailException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        final ResultCustomInfoEntity entity = apiInterface.get(seq);
        model.addAttribute("entity", entity);

        final MaindbGroupDetailResponse customGroup = maindbGroupApiInterface.get(entity.getGroupId());
        model.addAttribute("customGroup", customGroup);

        final CommonTypeEntity resultType = commonTypeApiInterface.get(customGroup.getResultType());
        resultType.setFields(resultType.getFields().stream().filter(e -> "Y".equals(e.getIsdisplay())).collect(Collectors.toList()));
        model.addAttribute("resultType", resultType);

        final CommonTypeEntity customDbType = commonTypeApiInterface.get(customGroup.getMaindbType());
        customDbType.setFields(customDbType.getFields().stream().filter(e -> "Y".equals(e.getIsdisplay())).collect(Collectors.toList()));
        model.addAttribute("customDbType", customDbType);

        final MaindbCustomInfoEntity customInfoData = maindbDataApiInterface.get(entity.getCustomId());

        final Map<String, Object> customFieldNameToValueMap = MaindbDataController.createFieldNameToValueMap(customInfoData, customDbType);
        model.addAttribute("customFieldNameToValueMap", customFieldNameToValueMap);

        final LinkedHashMap<String, Object> fieldNameToValueMap = createFieldNameToValueMap(entity, resultType);
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

        return "admin/application/maindb/result/modal";
    }

    @GetMapping("{seq}/modal-records")
    public String popupRecords(Model model, @PathVariable Integer seq) throws IOException, ResultFailException {
        final ResultCustomInfoEntity entity = apiInterface.get(seq);
        model.addAttribute("entity", entity);

        if (StringUtils.isNotEmpty(entity.getUniqueid())) {
            final List<RecordFile> files = apiInterface.getFiles(entity.getUniqueid());
            model.addAttribute("files", files);
        }

        return "admin/application/maindb/result/modal-records";
    }

    @GetMapping("_excel")
    public void downloadExcel(MaindbResultSearch search, HttpServletResponse response) throws IOException, ResultFailException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (search.getGroupSeq() == null) {
            final List<SearchMaindbGroupResponse> customdbGroups = apiInterface.customdbGroup();
            if (customdbGroups.size() == 0)
                throw new IllegalStateException("고객DB그룹이 존재하지 않습니다.");

            search.setGroupSeq(customdbGroups.get(0).getSeq());
        }
        search.setPage(1);
        search.setLimit(10000);
        final Pagination<ResultCustomInfoEntity> pagination = apiInterface.getPagination(search.getGroupSeq(), search.convertToRequest(""));

        final MaindbGroupDetailResponse customGroup = maindbGroupApiInterface.get(search.getGroupSeq());
        final CommonTypeEntity customDbType = commonTypeApiInterface.get(customGroup.getMaindbType());
        final CommonTypeEntity resultType = commonTypeApiInterface.get(customGroup.getResultType());

        new MaindbResultExcel(pagination.getRows(), customDbType, resultType).generator(response, "상담이력");
    }
}
