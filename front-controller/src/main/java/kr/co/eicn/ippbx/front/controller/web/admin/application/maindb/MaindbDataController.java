package kr.co.eicn.ippbx.front.controller.web.admin.application.maindb;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.model.search.MaindbDataSearch;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.application.maindb.MaindbDataApiInterface;
import kr.co.eicn.ippbx.front.service.api.application.maindb.MaindbGroupApiInterface;
import kr.co.eicn.ippbx.front.service.api.application.type.CommonTypeApiInterface;
import kr.co.eicn.ippbx.front.service.api.talk.group.TalkReceptionGroupApiInterface;
import kr.co.eicn.ippbx.front.service.excel.MaindbDataExcel;
import kr.co.eicn.ippbx.util.FormUtils;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonMaindbCustomInfo;
import kr.co.eicn.ippbx.model.dto.eicn.MaindbGroupDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.SummaryTalkServiceResponse;
import kr.co.eicn.ippbx.model.dto.eicn.search.SearchMaindbGroupResponse;
import kr.co.eicn.ippbx.model.entity.customdb.MaindbCustomInfoEntity;
import kr.co.eicn.ippbx.model.entity.eicn.CommonCodeEntity;
import kr.co.eicn.ippbx.model.entity.eicn.CommonFieldEntity;
import kr.co.eicn.ippbx.model.entity.eicn.CommonTypeEntity;
import kr.co.eicn.ippbx.model.enums.MultichannelChannelType;
import kr.co.eicn.ippbx.model.form.MaindbCustomInfoFormRequest;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author tinywind
 */
@AllArgsConstructor
@LoginRequired
@Controller
@RequestMapping("admin/application/maindb/data")
public class MaindbDataController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(MaindbDataController.class);

    private final MaindbDataApiInterface apiInterface;
    private final MaindbGroupApiInterface maindbGroupApiInterface;
    private final CommonTypeApiInterface commonTypeApiInterface;
    private final TalkReceptionGroupApiInterface talkReceptionGroupApiInterface;

    public static Map<String, Map<String, Object>> createCustomIdToFieldNameToValueMap(List<CommonMaindbCustomInfo> list, CommonTypeEntity customDbType) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        final Map<String, Map<String, Object>> customIdToFieldNameToValueMap = new HashMap<>();

        for (CommonMaindbCustomInfo row : list) {
            customIdToFieldNameToValueMap.put(row.getMaindbSysCustomId(), createFieldNameToValueMap(row, customDbType));
        }
        return customIdToFieldNameToValueMap;
    }

    public static Map<String, Object> createFieldNameToValueMap(CommonMaindbCustomInfo entity, CommonTypeEntity customDbType) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        final Map<String, Object> fieldNameToValueMap = new HashMap<>();

        final String removingPrefix = customDbType.getKind() + "_";
        for (CommonFieldEntity field : customDbType.getFields()) {
            final String fieldName = field.getFieldId().substring(removingPrefix.length()).toLowerCase();

            final String capName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            fieldNameToValueMap.put(field.getFieldId(), CommonMaindbCustomInfo.class.getDeclaredMethod("getMaindb" + capName).invoke(entity));
        }

        return fieldNameToValueMap;
    }

    @SuppressWarnings("unchecked")
    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") MaindbDataSearch search) throws IOException, ResultFailException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final List<SearchMaindbGroupResponse> customdbGroups = apiInterface.customdbGroup();
        if (customdbGroups.size() != 0) {
            /*throw new IllegalStateException("고객DB그룹이 존재하지 않습니다.");*/

            final Map<Integer, String> groups = customdbGroups.stream().collect(Collectors.toMap(SearchMaindbGroupResponse::getSeq, SearchMaindbGroupResponse::getName));
            model.addAttribute("customdbGroups", groups);

            if (search.getGroupSeq() == null)
                search.setGroupSeq(customdbGroups.get(0).getSeq());

            final Pagination<MaindbCustomInfoEntity> pagination = apiInterface.getPagination(search.getGroupSeq(), search.convertToRequest("MAINDB_"));
            model.addAttribute("pagination", pagination);

            final MaindbGroupDetailResponse customGroup = maindbGroupApiInterface.get(search.getGroupSeq());
            final CommonTypeEntity customDbType = commonTypeApiInterface.get(customGroup.getMaindbType());
            customDbType.setFields(customDbType.getFields().stream().filter(e -> "Y".equals(e.getIsdisplayList())).collect(Collectors.toList()));
            model.addAttribute("customDbType", customDbType);

            model.addAttribute("customIdToFieldNameToValueMap", createCustomIdToFieldNameToValueMap((List<CommonMaindbCustomInfo>) (List<?>) pagination.getRows(), customDbType));
        }
        return "admin/application/maindb/data/ground";
    }

    @GetMapping("new/modal")
    public String modal(Model model, @ModelAttribute("form") MaindbCustomInfoFormRequest form, @RequestParam Integer groupSeq) throws IOException, ResultFailException {
        final MaindbGroupDetailResponse customGroup = maindbGroupApiInterface.get(groupSeq);
        model.addAttribute("customGroup", customGroup);

        final CommonTypeEntity customDbType = commonTypeApiInterface.get(customGroup.getMaindbType());
        customDbType.setFields(customDbType.getFields().stream().filter(e -> "Y".equals(e.getIsdisplay())).collect(Collectors.toList()));
        model.addAttribute("customDbType", customDbType);

        final Map<String, String> channelTypes = FormUtils.options(MultichannelChannelType.class);
        model.addAttribute("channelTypes", channelTypes);

        form.setGroupSeq(groupSeq);

        final Map<String, String> fieldToRelatedField = new HashMap<>();
        for (CommonFieldEntity field : customDbType.getFields()) {
            if (field.getCodes() != null) {
                for (CommonCodeEntity code : field.getCodes()) {
                    if (StringUtils.isNotEmpty(code.getRelatedFieldId())) {
                        fieldToRelatedField.put(field.getFieldId(), code.getRelatedFieldId());
                    }
                }
            }
        }
        model.addAttribute("fieldToRelatedField", fieldToRelatedField);

        return "admin/application/maindb/data/modal";
    }

    @GetMapping("{customId}/modal")
    public String modal(Model model, @PathVariable String customId, @ModelAttribute("form") MaindbCustomInfoFormRequest form) throws IOException, ResultFailException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        final MaindbCustomInfoEntity entity = apiInterface.get(customId);
        model.addAttribute("entity", entity);

        form.setGroupSeq(entity.getMaindbSysGroupId());

        final Map<String, String> talkServices = talkReceptionGroupApiInterface.talkServices().stream().collect(Collectors.toMap(SummaryTalkServiceResponse::getSenderKey, SummaryTalkServiceResponse::getKakaoServiceName));
        model.addAttribute("talkServices", talkServices);

        final MaindbGroupDetailResponse customGroup = maindbGroupApiInterface.get(entity.getMaindbSysGroupId());
        model.addAttribute("customGroup", customGroup);

        final CommonTypeEntity customDbType = commonTypeApiInterface.get(customGroup.getMaindbType());
        customDbType.setFields(customDbType.getFields().stream().filter(e -> "Y".equals(e.getIsdisplay())).collect(Collectors.toList()));
        model.addAttribute("customDbType", customDbType);

        final Map<String, Object> fieldNameToValueMap = createFieldNameToValueMap(entity, customDbType);
        model.addAttribute("fieldNameToValueMap", fieldNameToValueMap);

        final Map<String, String> channelTypes = FormUtils.options(MultichannelChannelType.class);
        model.addAttribute("channelTypes", channelTypes);

        return "admin/application/maindb/data/modal";
    }

    @GetMapping("_excel")
    public void downloadExcel(MaindbDataSearch search, HttpServletResponse response) throws IOException, ResultFailException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (search.getGroupSeq() == null) {
            final List<SearchMaindbGroupResponse> customdbGroups = apiInterface.customdbGroup();
            if (customdbGroups.size() == 0)
                throw new IllegalStateException("고객DB그룹이 존재하지 않습니다.");

            search.setGroupSeq(customdbGroups.get(0).getSeq());
        }
        search.setPage(1);
        search.setLimit(10000);
        final Pagination<MaindbCustomInfoEntity> pagination = apiInterface.getPagination(search.getGroupSeq(), search.convertToRequest("MAINDB_"));

        final MaindbGroupDetailResponse customGroup = maindbGroupApiInterface.get(search.getGroupSeq());
        final CommonTypeEntity customDbType = commonTypeApiInterface.get(customGroup.getMaindbType());

        new MaindbDataExcel(pagination.getRows(), customDbType).generator(response, "고객정보");
    }
}
