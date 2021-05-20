package kr.co.eicn.ippbx.front.controller.web.admin.application.type;

import kr.co.eicn.ippbx.util.ReflectionUtils;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.application.type.CommonTypeApiInterface;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CommonBasicField;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CommonField;
import kr.co.eicn.ippbx.model.entity.eicn.CommonTypeEntity;
import kr.co.eicn.ippbx.model.enums.CommonTypeKind;
import kr.co.eicn.ippbx.model.form.CommonFieldFormRequest;
import kr.co.eicn.ippbx.model.form.CommonTypeFormRequest;
import kr.co.eicn.ippbx.model.form.CommonTypeUpdateFormRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author tinywind
 */
@LoginRequired
@Controller
@RequestMapping("admin/application/type/link-db")
public class LinkDbTypeController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(LinkDbTypeController.class);

    @Autowired
    private CommonTypeApiInterface apiInterface;

    @GetMapping("")
    public String page(Model model) throws IOException, ResultFailException {
        final List<CommonTypeEntity> list = apiInterface.list(CommonTypeKind.LINK_DB.getCode());
        model.addAttribute("list", list);
        return "admin/application/type/link-db/ground";
    }

    @GetMapping("new/modal")
    public String modal(Model model, @ModelAttribute("form") CommonTypeFormRequest form) throws IOException, ResultFailException {
        form.setKind(CommonTypeKind.LINK_DB.getCode());
        return "admin/application/type/link-db/modal-new-type";
    }

    @GetMapping("{seq}/modal")
    public String modal(Model model, @ModelAttribute("form") CommonTypeUpdateFormRequest form, @PathVariable Integer seq) throws IOException, ResultFailException {
        final CommonTypeEntity entity = apiInterface.get(seq);
        model.addAttribute("entity", entity);
        ReflectionUtils.copy(form, entity);
        form.setKind(CommonTypeKind.LINK_DB.getCode());

        final List<CommonBasicField> basicFields = apiInterface.getBasicFields(CommonTypeKind.LINK_DB.getCode());
        final List<CommonFieldFormRequest> fields = basicFields.stream().map(e -> {
            final CommonFieldFormRequest field = new CommonFieldFormRequest();

            field.setId(e.getId());
            field.setFieldSize("STRING".equals(e.getType()) ? "Y".equals(e.getIsdefault())
                    ? 100 : 30
                        : e.getFieldSize());
            field.setIsneed("Y".equals(e.getIsdefault()) ? "Y" : "N");
            field.setIsdisplay("Y");
            field.setIsdisplayList("Y");
            field.setIssearch("Y".equals(e.getIsdefault()) ? "Y" : "N");

            entity.getFields().stream().filter(f -> f.getFieldId().substring(entity.getKind().length() + 1).equals(field.getId())).findAny().ifPresent(f -> {
                field.setFieldName(f.getFieldInfo());
                field.setFieldSize(f.getFieldSize());
                field.setIsneed(f.getIsneed());
                field.setIsdisplay(f.getIsdisplay());
                field.setIsdisplayList(f.getIsdisplayList());
                field.setIssearch(f.getIssearch());
            });

            return field;
        }).collect(Collectors.toList());
        model.addAttribute("fields", fields);

        final Map<String, String> fieldIdToNames = basicFields.stream().collect(Collectors.toMap(CommonBasicField::getId, CommonBasicField::getName));
        model.addAttribute("fieldIdToNames", fieldIdToNames);

        final Map<String, String> fieldIdToTypes = basicFields.stream().collect(Collectors.toMap(CommonBasicField::getId, CommonBasicField::getType));
        model.addAttribute("fieldIdToTypes", fieldIdToTypes);

        final List<String> registeredFieldIds = entity.getFields().stream().map(e -> e.getFieldId().substring(entity.getKind().length() + 1)).collect(Collectors.toList());
        model.addAttribute("registeredFieldIds", registeredFieldIds);

        final List<String> defaultFieldIds = basicFields.stream().filter(e -> Objects.equals(e.getIsdefault(), "Y")).map(CommonBasicField::getId).collect(Collectors.toList());
        model.addAttribute("defaultFieldIds", defaultFieldIds);

        return "admin/application/type/link-db/modal-update-type";
    }

    @GetMapping("{seq}/modal-update-sequence-fields")
    public String modal(Model model, @PathVariable Integer seq) throws IOException, ResultFailException {
        final CommonTypeEntity entity = apiInterface.get(seq);
        model.addAttribute("entity", entity);
        entity.getFields().sort(Comparator.comparingInt(CommonField::getDisplaySeq));

        return "admin/application/type/link-db/modal-update-sequence-fields";
    }
}
