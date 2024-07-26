package kr.co.eicn.ippbx.front.controller.web.counsel;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.controller.web.admin.application.maindb.MaindbResultController;
import kr.co.eicn.ippbx.front.controller.web.admin.outbound.preview.PreviewDataController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.model.search.PreviewDataSearch;
import kr.co.eicn.ippbx.util.MapToLinkedHashMap;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.application.type.CommonTypeApiInterface;
import kr.co.eicn.ippbx.front.service.api.outbound.preview.PreviewDataApiInterface;
import kr.co.eicn.ippbx.front.service.api.outbound.preview.PreviewGroupApiInterface;
import kr.co.eicn.ippbx.front.service.api.record.callback.CallbackHistoryApiInterface;
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
import kr.co.eicn.ippbx.model.enums.MultichannelChannelType;
import kr.co.eicn.ippbx.model.form.PrvCustomInfoFormRequest;
import kr.co.eicn.ippbx.model.form.ResultCustomInfoFormRequest;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
@RequestMapping("counsel/preview")
public class CounselPreviewController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(CounselPreviewController.class);

    private final CommonTypeApiInterface commonTypeApiInterface;
    private final CallbackHistoryApiInterface callbackHistoryApiInterface;
    private final PreviewDataApiInterface previewDataApiInterface;
    private final PreviewGroupApiInterface previewGroupApiInterface;

    @GetMapping("")
    public String previewTab(Model model) throws IOException, ResultFailException {
        final List<PrvGroup> previewGroups = previewDataApiInterface.prvGroup();
        if (previewGroups.isEmpty())
            return "counsel/preview/body";

        final PrvGroup prvGroup = previewGroups.get(0);
        final CommonTypeEntity previewType = commonTypeApiInterface.get(prvGroup.getPrvType());
        model.addAttribute("previewType", previewType);
        final CommonTypeEntity resultType = commonTypeApiInterface.get(prvGroup.getResultType());
        model.addAttribute("resultType", resultType);

        return "counsel/preview/body";
    }

    @SuppressWarnings("unchecked")
    @GetMapping("list-body")
    public String previewListBody(Model model, @ModelAttribute("search") PreviewDataSearch search) throws IOException, ResultFailException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        final List<PrvGroup> previewGroups = previewDataApiInterface.prvGroup();
        if (CollectionUtils.isEmpty(previewGroups))
            return "counsel/preview/list-body";

        final Map<Integer, String> groups = previewGroups.stream().collect(Collectors.toMap(PrvGroup::getSeq, PrvGroup::getName));
        model.addAttribute("previewGroups", new MapToLinkedHashMap().toLinkedHashMapByValue(groups));

        if (search.getGroupSeq() == null)
            search.setGroupSeq(previewGroups.get(0).getSeq());

        //상담원일 경우 ID 강제 적용
        if (g.getUser().getIdType().equals("M"))
            search.setPersonIdInCharge(g.getUser().getId());

        final Optional<PrvGroup> groupOptional = previewGroups.stream().filter(e -> Objects.equals(e.getSeq(), search.getGroupSeq())).findFirst();
        if (!groupOptional.isPresent())
            return "counsel/preview/list-body";

        final PrvGroup prvGroup = groupOptional.get();

        final Pagination<PrvCustomInfoEntity> pagination = previewDataApiInterface.getPagination(search.getGroupSeq(), search.convertToRequest(""));
        model.addAttribute("pagination", pagination);

        final CommonTypeEntity previewType = commonTypeApiInterface.get(prvGroup.getPrvType());
        model.addAttribute("previewType", previewType);
        final CommonTypeEntity resultType = commonTypeApiInterface.get(prvGroup.getResultType());
        model.addAttribute("resultType", resultType);

        model.addAttribute("seqToFieldNameToValueMap", MaindbResultController.createSeqToFieldNameToValueMap(pagination.getRows().stream().map(PrvCustomInfoEntity::getResult).filter(Objects::nonNull).collect(Collectors.toList()), resultType));
        model.addAttribute("customIdToFieldNameToValueMap", PreviewDataController.createCustomIdToFieldNameToValueMap(pagination.getRows(), previewType));

        final Map<String, String> persons = callbackHistoryApiInterface.addPersons().stream().collect(Collectors.toMap(SummaryCallbackDistPersonResponse::getId, SummaryPersonResponse::getIdName));
        model.addAttribute("persons", new MapToLinkedHashMap().toLinkedHashMapByValue(persons));

        return "counsel/preview/list-body";
    }

    @GetMapping("custom-input")
    public String previewCustomInput(Model model, @ModelAttribute("form") PrvCustomInfoFormRequest form, @RequestParam Integer groupSeq, @RequestParam(required = false) String customId) throws IOException, ResultFailException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        form.setGroupSeq(groupSeq);

        final PrvGroupDetailResponse previewGroup = previewGroupApiInterface.get(groupSeq);
        model.addAttribute("previewGroup", previewGroup);

        final CommonTypeEntity previewType = commonTypeApiInterface.get(previewGroup.getPrvType());
        model.addAttribute("previewType", previewType);

        if (StringUtils.isNotEmpty(customId)) {
            final PrvCustomInfoEntity entity = previewDataApiInterface.get(groupSeq, customId);
            model.addAttribute("entity", entity);

            final Map<String, Object> fieldNameToValueMap = PreviewDataController.createFieldNameToValueMap(entity, previewType);
            model.addAttribute("fieldNameToValueMap", fieldNameToValueMap);
        }

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

        return "counsel/preview/custom-input";
    }

    @GetMapping("counseling-input")
    public String previewCounselingInput(Model model, @ModelAttribute("form") ResultCustomInfoFormRequest form, @RequestParam Integer groupSeq, @RequestParam(required = false) String customId) throws IOException, ResultFailException {
        final PrvGroupDetailResponse previewGroup = previewGroupApiInterface.get(groupSeq);
        model.addAttribute("previewGroup", previewGroup);

        final CommonTypeEntity resultType = commonTypeApiInterface.get(previewGroup.getResultType());
        model.addAttribute("resultType", resultType);

        form.setGroupId(groupSeq);
        form.setCustomId(customId);
        form.setMaindbType(previewGroup.getPrvType());
        form.setResultType(previewGroup.getResultType());
        form.setGroupKind(MultichannelChannelType.PHONE.getCode());

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


        return "counsel/preview/counseling-input";
    }

}
