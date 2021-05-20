package kr.co.eicn.ippbx.front.controller.web.admin.record.file;

import kr.co.eicn.ippbx.util.ReflectionUtils;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.record.file.RecordFileEncApiInterface;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.RecordEnc;
import kr.co.eicn.ippbx.model.dto.eicn.RecordEncKeyResponse;
import kr.co.eicn.ippbx.model.dto.eicn.RecordEncKeySummaryResponse;
import kr.co.eicn.ippbx.model.form.RecordEncKeyFormRequest;
import kr.co.eicn.ippbx.model.search.RecordEncSearchRequest;
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

/**
 * @author tinywind
 */
@LoginRequired
@Controller
@RequestMapping("admin/record/file/enc")
public class RecordFileEncController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(RecordFileEncController.class);

    @Autowired
    private RecordFileEncApiInterface apiInterface;

    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") RecordEncSearchRequest search) throws IOException, ResultFailException {
        final RecordEnc encType = apiInterface.getCurrentEncryptionType();
        model.addAttribute("encType", encType);

        final Pagination<RecordEncKeySummaryResponse> pagination = apiInterface.pagination(new RecordEncSearchRequest());
        model.addAttribute("pagination", pagination);

        return "admin/record/file/enc/ground";
    }

    @GetMapping("key/new/modal")
    public String modal(Model model, @ModelAttribute("form") RecordEncKeyFormRequest form) {
        return "admin/record/file/enc/modal";
    }

    @GetMapping("key/{id}/modal")
    public String modal(Model model, @ModelAttribute("form") RecordEncKeyFormRequest form, @PathVariable Integer id) throws IOException, ResultFailException {
        final RecordEncKeyResponse entity = apiInterface.get(id);
        model.addAttribute("entity", entity);
        ReflectionUtils.copy(form, entity);

        return modal(model, form);
    }
}
