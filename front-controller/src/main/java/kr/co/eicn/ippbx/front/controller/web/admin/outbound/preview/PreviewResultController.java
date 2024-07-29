package kr.co.eicn.ippbx.front.controller.web.admin.outbound.preview;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.controller.web.admin.application.maindb.MaindbResultController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.model.search.PreviewResultSearch;
import kr.co.eicn.ippbx.util.MapToLinkedHashMap;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.application.type.CommonTypeApiInterface;
import kr.co.eicn.ippbx.front.service.api.outbound.preview.PreviewDataApiInterface;
import kr.co.eicn.ippbx.front.service.api.outbound.preview.PreviewGroupApiInterface;
import kr.co.eicn.ippbx.front.service.api.outbound.preview.PreviewResultApiInterface;
import kr.co.eicn.ippbx.front.service.api.record.callback.CallbackHistoryApiInterface;
import kr.co.eicn.ippbx.front.service.excel.PreviewResultExcel;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonResultCustomInfo;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PrvGroup;
import kr.co.eicn.ippbx.model.dto.eicn.PrvGroupDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.SummaryCallbackDistPersonResponse;
import kr.co.eicn.ippbx.model.dto.eicn.SummaryPersonResponse;
import kr.co.eicn.ippbx.model.entity.customdb.PrvResultCustomInfoEntity;
import kr.co.eicn.ippbx.model.entity.eicn.CommonCodeEntity;
import kr.co.eicn.ippbx.model.entity.eicn.CommonFieldEntity;
import kr.co.eicn.ippbx.model.entity.eicn.CommonTypeEntity;
import kr.co.eicn.ippbx.model.form.ResultCustomInfoFormRequest;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jooq.tools.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
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
@RequestMapping("admin/outbound/preview/result")
public class PreviewResultController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(PreviewResultController.class);

    private final PreviewResultApiInterface apiInterface;
    private final PreviewDataApiInterface previewDataApiInterface;
    private final PreviewGroupApiInterface previewGroupApiInterface;
    private final CommonTypeApiInterface commonTypeApiInterface;
    private final CallbackHistoryApiInterface callbackHistoryApiInterface;

    @SuppressWarnings("unchecked")
    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") PreviewResultSearch search) throws IOException, ResultFailException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final List<PrvGroup> previewGroups = previewDataApiInterface.prvGroup();
        if (!CollectionUtils.isEmpty(previewGroups)) {
            final Map<Integer, String> groups = previewGroups.stream().collect(Collectors.toMap(PrvGroup::getSeq, PrvGroup::getName));
            model.addAttribute("previewGroups", new MapToLinkedHashMap().toLinkedHashMapByValue(groups));

            if (search.getGroupSeq() == null)
                return "admin/outbound/preview/result/ground";
                //search.setGroupSeq(previewGroups.get(0).getSeq());

            final Optional<PrvGroup> groupOptional = previewGroups.stream().filter(e -> Objects.equals(e.getSeq(), search.getGroupSeq())).findFirst();
            if (groupOptional.isEmpty())
                throw new IllegalArgumentException("존재하지 않는 프리뷰 그룹입니다.(seq: " + search.getGroupSeq() + ")");

            final PrvGroup prvGroup = groupOptional.get();

            final Pagination<PrvResultCustomInfoEntity> pagination = apiInterface.getPagination(search.getGroupSeq(), search.convertToRequest(""));
            model.addAttribute("pagination", pagination);

            final CommonTypeEntity previewType = commonTypeApiInterface.get(prvGroup.getPrvType());
            model.addAttribute("previewType", previewType);
            final CommonTypeEntity resultType = commonTypeApiInterface.get(prvGroup.getResultType());
            model.addAttribute("resultType", resultType);

            model.addAttribute("seqToFieldNameToValueMap", MaindbResultController.createSeqToFieldNameToValueMap(pagination.getRows(), resultType));
            model.addAttribute("customIdToFieldNameToValueMap", PreviewDataController.createCustomIdToFieldNameToValueMap(pagination.getRows().stream().map(PrvResultCustomInfoEntity::getCustomInfo).collect(Collectors.toList()), previewType));

            final Map<String, String> persons = callbackHistoryApiInterface.addPersons().stream().collect(Collectors.toMap(SummaryCallbackDistPersonResponse::getId, SummaryPersonResponse::getIdName));
            model.addAttribute("persons", new MapToLinkedHashMap().toLinkedHashMapByValue(persons));
        }
        return "admin/outbound/preview/result/ground";
    }

    @GetMapping("{groupSeq}/data/{seq}/modal")
    public String modal(Model model, @PathVariable Integer groupSeq, @PathVariable Integer seq, @ModelAttribute("form") ResultCustomInfoFormRequest form) throws IOException, ResultFailException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        final PrvResultCustomInfoEntity entity = apiInterface.get(groupSeq, seq);
        model.addAttribute("entity", entity);

        form.setGroupId(entity.getCustomInfo().getPrvSysGroupId());

        final PrvGroupDetailResponse previewGroup = previewGroupApiInterface.get(entity.getCustomInfo().getPrvSysGroupId());
        model.addAttribute("previewGroup", previewGroup);

        final CommonTypeEntity resultType = commonTypeApiInterface.get(previewGroup.getResultType());
        model.addAttribute("resultType", resultType);

        final Map<String, Object> fieldNameToValueMap = MaindbResultController.createFieldNameToValueMap(entity, resultType);
        model.addAttribute("fieldNameToValueMap", fieldNameToValueMap);

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

        return "admin/outbound/preview/result/modal";
    }

    @GetMapping("_excel")
    public void downloadExcel(PreviewResultSearch search, HttpServletResponse response) throws IOException, ResultFailException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        final List<PrvGroup> previewGroups = previewDataApiInterface.prvGroup();
        if (CollectionUtils.isEmpty(previewGroups))
            throw new IllegalStateException("프리뷰 그룹이 존재하지 않습니다.");

        if (search.getGroupSeq() == null)
            search.setGroupSeq(previewGroups.get(0).getSeq());

        final Optional<PrvGroup> groupOptional = previewGroups.stream().filter(e -> Objects.equals(e.getSeq(), search.getGroupSeq())).findFirst();
        if (groupOptional.isEmpty())
            throw new IllegalArgumentException("존재하지 않는 프리뷰 그룹입니다.(seq: " + search.getGroupSeq() + ")");

        final PrvGroup prvGroup = groupOptional.get();

        search.setPage(1);
        search.setLimit(10000);
        final Pagination<PrvResultCustomInfoEntity> pagination = apiInterface.getPagination(search.getGroupSeq(), search.convertToRequest(""));

        final CommonTypeEntity previewType = commonTypeApiInterface.get(prvGroup.getPrvType());
        final CommonTypeEntity resultType = commonTypeApiInterface.get(prvGroup.getResultType());

        new PreviewResultExcel(pagination.getRows(), previewType, resultType).generator(response, "프리뷰결과이력");
    }
}
