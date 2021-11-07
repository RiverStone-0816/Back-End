package kr.co.eicn.ippbx.front.controller.web.admin.outbound.preview;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.controller.web.admin.application.maindb.MaindbResultController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.model.search.PreviewDataSearch;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.application.type.CommonTypeApiInterface;
import kr.co.eicn.ippbx.front.service.api.outbound.preview.PreviewGroupApiInterface;
import kr.co.eicn.ippbx.front.service.api.outbound.preview.PreviewDataApiInterface;
import kr.co.eicn.ippbx.front.service.api.record.callback.CallbackHistoryApiInterface;
import kr.co.eicn.ippbx.front.service.excel.PreviewDataExcel;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonPrvCustomInfo;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PrvGroup;
import kr.co.eicn.ippbx.model.dto.eicn.PrvGroupDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.SummaryCallbackDistPersonResponse;
import kr.co.eicn.ippbx.model.dto.eicn.SummaryPersonResponse;
import kr.co.eicn.ippbx.model.entity.customdb.PrvCustomInfoEntity;
import kr.co.eicn.ippbx.model.entity.eicn.CommonCodeEntity;
import kr.co.eicn.ippbx.model.entity.eicn.CommonFieldEntity;
import kr.co.eicn.ippbx.model.entity.eicn.CommonTypeEntity;
import kr.co.eicn.ippbx.model.form.MaindbCustomInfoFormRequest;
import kr.co.eicn.ippbx.model.form.PrvCustomInfoRedistributionFormRequest;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author tinywind
 */
@AllArgsConstructor
@LoginRequired
@Controller
@RequestMapping("admin/outbound/preview/data")
public class PreviewDataController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(PreviewDataController.class);

    private final PreviewDataApiInterface apiInterface;
    private final PreviewGroupApiInterface previewGroupApiInterface;
    private final CommonTypeApiInterface commonTypeApiInterface;
    private final CallbackHistoryApiInterface callbackHistoryApiInterface;

    public static Map<String, Map<String, Object>> createCustomIdToFieldNameToValueMap(List<CommonPrvCustomInfo> list, CommonTypeEntity previewType) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        final Map<String, Map<String, Object>> customIdToFieldNameToValueMap = new HashMap<>();

        for (CommonPrvCustomInfo row : list) {
            customIdToFieldNameToValueMap.put(row.getPrvSysCustomId(), createFieldNameToValueMap(row, previewType));
        }
        return customIdToFieldNameToValueMap;
    }

    public static Map<String, Object> createFieldNameToValueMap(CommonPrvCustomInfo entity, CommonTypeEntity previewType) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        final Map<String, Object> fieldNameToValueMap = new HashMap<>();

        final String removingPrefix = previewType.getKind() + "_";
        for (CommonFieldEntity field : previewType.getFields()) {
            final String fieldName = field.getFieldId().substring(removingPrefix.length()).toLowerCase();

            final String capName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            fieldNameToValueMap.put(field.getFieldId(), CommonPrvCustomInfo.class.getDeclaredMethod("getPrv" + capName).invoke(entity));
        }

        return fieldNameToValueMap;
    }

    @SuppressWarnings("unchecked")
    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") PreviewDataSearch search) throws IOException, ResultFailException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final List<PrvGroup> previewGroups = apiInterface.prvGroup();
        if (previewGroups.size() != 0) {

            //상담원일 경우 ID 강제 적용
            if (g.getUser().getIdType().equals("M"))
                search.setPersonIdInCharge(g.getUser().getId());

            final Map<Integer, String> groups = previewGroups.stream().collect(Collectors.toMap(PrvGroup::getSeq, PrvGroup::getName));
            model.addAttribute("previewGroups", groups);

            final PrvGroup prvGroup1 = previewGroups.stream().filter(e -> Objects.equals(e.getSeq(), search.getGroupSeq())).findFirst().orElse(null);

            if (prvGroup1 != null) {
                final Pagination<PrvCustomInfoEntity> pagination = apiInterface.getPagination(search.getGroupSeq(), search.convertToRequest(""));
                model.addAttribute("pagination", pagination);

                final CommonTypeEntity previewType = commonTypeApiInterface.get(prvGroup1.getPrvType());
                model.addAttribute("previewType", previewType);
                final CommonTypeEntity resultType = commonTypeApiInterface.get(prvGroup1.getResultType());
                model.addAttribute("resultType", resultType);

                model.addAttribute("seqToFieldNameToValueMap", MaindbResultController.createSeqToFieldNameToValueMap(pagination.getRows().stream().map(PrvCustomInfoEntity::getResult).filter(Objects::nonNull).collect(Collectors.toList()), resultType));
                model.addAttribute("customIdToFieldNameToValueMap", createCustomIdToFieldNameToValueMap((List<CommonPrvCustomInfo>) (List<?>) pagination.getRows(), previewType));
            }

            final Map<String, String> persons = callbackHistoryApiInterface.addPersons().stream().collect(Collectors.toMap(SummaryCallbackDistPersonResponse::getId, SummaryPersonResponse::getIdName));
            model.addAttribute("persons", persons);
        }
        return "admin/outbound/preview/data/ground";
    }

    @GetMapping("modal-redistribution")
    public String modalRedistribution(Model model, @ModelAttribute("form") PrvCustomInfoRedistributionFormRequest form, @RequestParam Integer groupSeq) throws IOException, ResultFailException {
        final List<SummaryCallbackDistPersonResponse> addOnPersons = callbackHistoryApiInterface.addPersons();
        model.addAttribute("addOnPersons", addOnPersons);
        model.addAttribute("groupSeq", groupSeq);

        return "admin/outbound/preview/data/modal-redistribution";
    }

    @GetMapping("{groupSeq}/data/new/modal")
    public String modal(Model model, @ModelAttribute("form") MaindbCustomInfoFormRequest form, @PathVariable Integer groupSeq) throws IOException, ResultFailException {
        final PrvGroupDetailResponse previewGroup = previewGroupApiInterface.get(groupSeq);
        model.addAttribute("previewGroup", previewGroup);

        final CommonTypeEntity previewType = commonTypeApiInterface.get(previewGroup.getPrvType());
        model.addAttribute("previewType", previewType);

        form.setGroupSeq(groupSeq);

        final Map<String, String> fieldToRelatedField = new HashMap<>();
        for (CommonFieldEntity field : previewType.getFields()) {
            if (field.getCodes() != null) {
                for (CommonCodeEntity code : field.getCodes()) {
                    if (StringUtils.isNotEmpty(code.getRelatedFieldId())) {
                        fieldToRelatedField.put(field.getFieldId(), code.getRelatedFieldId());
                    }
                }
            }
        }
        model.addAttribute("fieldToRelatedField", fieldToRelatedField);

        return "admin/outbound/preview/data/modal";
    }

    @GetMapping("{groupSeq}/data/{customId}/modal")
    public String modal(Model model, @PathVariable Integer groupSeq, @PathVariable String customId, @ModelAttribute("form") MaindbCustomInfoFormRequest form) throws IOException, ResultFailException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        final PrvCustomInfoEntity entity = apiInterface.get(groupSeq, customId);
        model.addAttribute("entity", entity);

        form.setGroupSeq(entity.getPrvSysGroupId());

        final PrvGroupDetailResponse previewGroup = previewGroupApiInterface.get(entity.getPrvSysGroupId());
        model.addAttribute("previewGroup", previewGroup);

        final CommonTypeEntity previewType = commonTypeApiInterface.get(previewGroup.getPrvType());
        model.addAttribute("previewType", previewType);

        final Map<String, Object> fieldNameToValueMap = createFieldNameToValueMap(entity, previewType);
        model.addAttribute("fieldNameToValueMap", fieldNameToValueMap);

        return "admin/outbound/preview/data/modal";
    }

    @GetMapping("_excel")
    public void downloadExcel(PreviewDataSearch search, HttpServletResponse response) throws IOException, ResultFailException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        final List<PrvGroup> previewGroups = apiInterface.prvGroup();
        if (previewGroups.size() == 0)
            throw new IllegalStateException("프리뷰 그룹이 존재하지 않습니다.");

        if (search.getGroupSeq() == null)
            search.setGroupSeq(previewGroups.get(0).getSeq());

        final Optional<PrvGroup> groupOptional = previewGroups.stream().filter(e -> Objects.equals(e.getSeq(), search.getGroupSeq())).findFirst();
        if (!groupOptional.isPresent())
            throw new IllegalArgumentException("존재하지 않는 프리뷰 그룹입니다.(seq: " + search.getGroupSeq() + ")");

        final PrvGroup prvGroup = groupOptional.get();

        search.setPage(1);
        search.setLimit(10000);
        final Pagination<PrvCustomInfoEntity> pagination = apiInterface.getPagination(search.getGroupSeq(), search.convertToRequest("PRV_"));

        final CommonTypeEntity previewType = commonTypeApiInterface.get(prvGroup.getPrvType());
        final CommonTypeEntity resultType = commonTypeApiInterface.get(prvGroup.getResultType());

        new PreviewDataExcel(pagination.getRows(), previewType, resultType).generator(response, "프리뷰 데이터");
    }
}
