package kr.co.eicn.ippbx.front.controller.web.admin.acd.grade;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.model.form.GradeFileForm;
import kr.co.eicn.ippbx.front.service.excel.example.RoutingUploadExampleExcel;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.acd.grade.GradelistApiInterface;
import kr.co.eicn.ippbx.util.ReflectionUtils;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.model.dto.eicn.search.SearchQueueResponse;
import kr.co.eicn.ippbx.model.entity.eicn.GradeListEntity;
import kr.co.eicn.ippbx.model.form.GradeListFormRequest;
import kr.co.eicn.ippbx.model.search.GradeListSearchRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author tinywind
 */
@LoginRequired
@Controller
@RequestMapping("admin/acd/grade/blacklist")
public class BlacklistRoutingController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(BlacklistRoutingController.class);

    @Autowired
    private GradelistApiInterface apiInterface;

    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") GradeListSearchRequest search) throws IOException, ResultFailException {
        search.setGrade("BLACK");
        search.setGradeNumbers(null);
        final Pagination<GradeListEntity> pagination = apiInterface.pagination(search);
        model.addAttribute("pagination", pagination);

        final Map<String, String> queues = apiInterface.queues().stream().collect(Collectors.toMap(SearchQueueResponse::getNumber, SearchQueueResponse::getHanName));
        model.addAttribute("queues", queues);

        return "admin/acd/grade/blacklist/ground";
    }

    @GetMapping("new/modal")
    public String modal(Model model, @ModelAttribute("form") GradeListFormRequest form) throws IOException, ResultFailException {
        final Map<String, String> queues = apiInterface.queues().stream().collect(Collectors.toMap(SearchQueueResponse::getNumber, SearchQueueResponse::getHanName));
        model.addAttribute("queues", queues);

        form.setGrade("BLACK");

        return "admin/acd/grade/blacklist/modal";
    }

    @GetMapping("{seq}/modal")
    public String modal(Model model, @PathVariable Integer seq, @ModelAttribute("form") GradeListFormRequest form) throws IOException, ResultFailException {
        final GradeListEntity entity = apiInterface.get(seq);
        model.addAttribute("entity", entity);
        ReflectionUtils.copy(form, entity);
        form.setQueueNumber(entity.getHuntNumber());

        final Map<String, String> queues = apiInterface.queues().stream().collect(Collectors.toMap(SearchQueueResponse::getNumber, SearchQueueResponse::getHanName));
        model.addAttribute("queues", queues);

        if (StringUtils.isNotEmpty(entity.getHuntNumber()) && !queues.containsKey(entity.getHuntNumber()))
            queues.put(entity.getHuntNumber(), entity.getHuntName());

        form.setGrade("BLACK");

        return "admin/acd/grade/blacklist/modal";
    }

    @GetMapping("modal-upload")
    public String modalUpload(@ModelAttribute("form") GradeFileForm form) {
        form.setGradeType("BLACK");
        return "admin/acd/grade/blacklist/modal-upload";
    }

    @GetMapping("_excel/example")
    public void downloadExcel(HttpServletResponse response) throws IOException, ResultFailException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        new RoutingUploadExampleExcel().generator(response, "라우팅_업로드_예시");
    }
}
