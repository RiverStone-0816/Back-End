package kr.co.eicn.ippbx.front.controller.web.admin.outbound.pds;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.model.search.PdsCustominfoSearch;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.application.type.CommonTypeApiInterface;
import kr.co.eicn.ippbx.front.service.api.outbound.pds.PdsCustominfoApiInterface;
import kr.co.eicn.ippbx.front.service.api.outbound.pds.PdsGroupApiInterface;
import kr.co.eicn.ippbx.front.service.excel.PdsCustominfoExcel;
import kr.co.eicn.ippbx.front.util.FormUtils;
import kr.co.eicn.ippbx.front.util.page.Pagination;
import kr.co.eicn.ippbx.server.jooq.pds.tables.pojos.PdsCustomInfo;
import kr.co.eicn.ippbx.server.model.dto.eicn.PDSGroupDetailResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.PDSGroupSummaryResponse;
import kr.co.eicn.ippbx.server.model.entity.eicn.CommonCodeEntity;
import kr.co.eicn.ippbx.server.model.entity.eicn.CommonFieldEntity;
import kr.co.eicn.ippbx.server.model.entity.eicn.CommonTypeEntity;
import kr.co.eicn.ippbx.server.model.entity.pds.PDSCustomInfoEntity;
import kr.co.eicn.ippbx.server.model.enums.MultichannelChannelType;
import kr.co.eicn.ippbx.server.model.form.MaindbCustomInfoFormRequest;
import kr.co.eicn.ippbx.server.model.search.PDSGroupSearchRequest;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author tinywind
 */
@AllArgsConstructor
@LoginRequired
@Controller
@RequestMapping("admin/outbound/pds/custominfo")
public class PdsCustominfoController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(PdsCustominfoController.class);
    private static final String DB_TYPE_FIELD_PREFIX = "PDS_";
    private static final String POJO_GETTER_PREFIX = "getPds";

    private final PdsCustominfoApiInterface apiInterface;
    private final PdsGroupApiInterface pdsGroupApiInterface;
    private final CommonTypeApiInterface commonTypeApiInterface;

    public static Map<String, Map<String, Object>> createCustomIdToFieldNameToValueMap(List<PdsCustomInfo> list, CommonTypeEntity pdsType) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        final Map<String, Map<String, Object>> customIdToFieldNameToValueMap = new HashMap<>();

        for (PdsCustomInfo row : list) {
            customIdToFieldNameToValueMap.put(row.getPdsSysCustomId(), createFieldNameToValueMap(row, pdsType));
        }
        return customIdToFieldNameToValueMap;
    }

    private static Map<String, Object> createFieldNameToValueMap(PdsCustomInfo entity, CommonTypeEntity pdsType) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        final Map<String, Object> fieldNameToValueMap = new HashMap<>();

        final String removingPrefix = pdsType.getKind() + "_";
        for (CommonFieldEntity field : pdsType.getFields()) {
            final String fieldName = field.getFieldId().substring(removingPrefix.length()).toLowerCase();

            final String capName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            fieldNameToValueMap.put(field.getFieldId(), PdsCustomInfo.class.getDeclaredMethod(POJO_GETTER_PREFIX + capName).invoke(entity));
        }

        return fieldNameToValueMap;
    }

    @SuppressWarnings("unchecked")
    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") PdsCustominfoSearch search) throws IOException, ResultFailException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final PDSGroupSearchRequest groupSearch = new PDSGroupSearchRequest();
        groupSearch.setLimit(10000);
        final List<PDSGroupSummaryResponse> groups = pdsGroupApiInterface.pagination(groupSearch).getRows();

        if (groups.size() != 0) {
            model.addAttribute("groups", groups.stream().collect(Collectors.toMap(PDSGroupSummaryResponse::getSeq, PDSGroupSummaryResponse::getName)));

            if (search.getGroupSeq() == null)
                search.setGroupSeq(groups.get(0).getSeq());

            final Pagination<PDSCustomInfoEntity> pagination = apiInterface.getPagination(search.getGroupSeq(), search.convertToRequest(DB_TYPE_FIELD_PREFIX));
            model.addAttribute("pagination", pagination);

            final PDSGroupDetailResponse group = pdsGroupApiInterface.get(search.getGroupSeq());
            final CommonTypeEntity pdsType = commonTypeApiInterface.get(group.getPdsType());
            pdsType.setFields(pdsType.getFields().stream().filter(e -> "Y".equals(e.getIsdisplayList())).collect(Collectors.toList()));
            model.addAttribute("pdsType", pdsType);

            model.addAttribute("customIdToFieldNameToValueMap", createCustomIdToFieldNameToValueMap((List<PdsCustomInfo>) (List<?>) pagination.getRows(), pdsType));
        }

        return "admin/outbound/pds/custominfo/ground";
    }

    @GetMapping(value = "new/modal", params = "groupSeq")
    public String modal(Model model, @ModelAttribute("form") MaindbCustomInfoFormRequest form) throws IOException, ResultFailException {
        final PDSGroupDetailResponse group = pdsGroupApiInterface.get(form.getGroupSeq());
        model.addAttribute("group", group);

        final CommonTypeEntity pdsType = commonTypeApiInterface.get(group.getPdsType());
        pdsType.setFields(pdsType.getFields().stream().filter(e -> "Y".equals(e.getIsdisplay())).collect(Collectors.toList()));
        model.addAttribute("pdsType", pdsType);

        final Map<String, String> channelTypes = FormUtils.options(MultichannelChannelType.class);
        model.addAttribute("channelTypes", channelTypes);

        final Map<String, String> fieldToRelatedField = new HashMap<>();
        for (CommonFieldEntity field : pdsType.getFields()) {
            if (field.getCodes() != null) {
                for (CommonCodeEntity code : field.getCodes()) {
                    if (StringUtils.isNotEmpty(code.getRelatedFieldId())) {
                        fieldToRelatedField.put(field.getFieldId(), code.getRelatedFieldId());
                    }
                }
            }
        }
        model.addAttribute("fieldToRelatedField", fieldToRelatedField);

        return "admin/outbound/pds/custominfo/modal";
    }

    @GetMapping(value = "{customId}/modal", params = "groupSeq")
    public String modal(Model model, @PathVariable String customId, @ModelAttribute("form") MaindbCustomInfoFormRequest form) throws IOException, ResultFailException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        final PDSCustomInfoEntity entity = apiInterface.get(form.getGroupSeq(), customId);
        model.addAttribute("entity", entity);

        final PDSGroupDetailResponse group = pdsGroupApiInterface.get(form.getGroupSeq());
        model.addAttribute("group", group);

        final CommonTypeEntity pdsType = commonTypeApiInterface.get(group.getPdsType());
        pdsType.setFields(pdsType.getFields().stream().filter(e -> "Y".equals(e.getIsdisplay())).collect(Collectors.toList()));
        model.addAttribute("pdsType", pdsType);

        final Map<String, Object> fieldNameToValueMap = createFieldNameToValueMap(entity, pdsType);
        model.addAttribute("fieldNameToValueMap", fieldNameToValueMap);

        final Map<String, String> channelTypes = FormUtils.options(MultichannelChannelType.class);
        model.addAttribute("channelTypes", channelTypes);

        return "admin/outbound/pds/custominfo/modal";
    }

    @GetMapping("_excel")
    public void downloadExcel(PdsCustominfoSearch search, HttpServletResponse response) throws IOException, ResultFailException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (search.getGroupSeq() == null) {
            final PDSGroupSearchRequest groupSearch = new PDSGroupSearchRequest();
            groupSearch.setLimit(10000);
            final List<PDSGroupSummaryResponse> groups = pdsGroupApiInterface.pagination(groupSearch).getRows();
            if (groups.size() == 0)
                throw new IllegalStateException("고객DB그룹이 존재하지 않습니다.");

            search.setGroupSeq(groups.get(0).getSeq());
        }
        search.setLimit(10000);
        final Pagination<PDSCustomInfoEntity> pagination = apiInterface.getPagination(search.getGroupSeq(), search.convertToRequest(DB_TYPE_FIELD_PREFIX));

        final PDSGroupDetailResponse group = pdsGroupApiInterface.get(search.getGroupSeq());
        final CommonTypeEntity pdsType = commonTypeApiInterface.get(group.getPdsType());

        new PdsCustominfoExcel(pagination.getRows(), pdsType).generator(response, "고객정보");
    }
}
