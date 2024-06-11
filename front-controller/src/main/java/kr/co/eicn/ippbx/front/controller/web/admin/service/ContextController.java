package kr.co.eicn.ippbx.front.controller.web.admin.service;

import kr.co.eicn.ippbx.model.enums.WebVoiceItemYn;
import kr.co.eicn.ippbx.util.ReflectionUtils;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.service.ContextApiInterface;
import kr.co.eicn.ippbx.model.dto.eicn.ContextInfoResponse;
import kr.co.eicn.ippbx.model.dto.eicn.WebVoiceItemsResponse;
import kr.co.eicn.ippbx.model.dto.eicn.WebVoiceResponse;
import kr.co.eicn.ippbx.model.form.ContextInfoFormRequest;
import kr.co.eicn.ippbx.model.form.WebVoiceItemsDtmfFormRequest;
import kr.co.eicn.ippbx.model.form.WebVoiceItemsFormRequest;
import kr.co.eicn.ippbx.model.form.WebVoiceItemsInputFormRequest;
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

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author tinywind
 */
@LoginRequired
@Controller
@RequestMapping("admin/service/context")
public class ContextController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(ContextController.class);

    @Autowired
    private ContextApiInterface apiInterface;

    @GetMapping("")
    public String page(Model model) throws IOException, ResultFailException {
        final List<ContextInfoResponse> list = apiInterface.list();
        model.addAttribute("list", list);

        return "admin/service/context/ground";
    }

    @GetMapping("new/modal")
    public String modal(Model model, @ModelAttribute("form") ContextInfoFormRequest form) throws IOException, ResultFailException {
        return "admin/service/context/modal";
    }

    @GetMapping("{seq}/modal")
    public String modal(Model model, @PathVariable Integer seq, @ModelAttribute("form") ContextInfoFormRequest form) throws IOException, ResultFailException {
        final ContextInfoResponse entity = apiInterface.get(seq);
        ReflectionUtils.copy(form, entity);
        model.addAttribute("entity", entity);

        return "admin/service/context/modal";
    }

    @GetMapping("{seq}/modal-visual-ars")
    public String modalVisualArs(Model model, @PathVariable Integer seq, @ModelAttribute("form") WebVoiceItemsFormRequest form) throws IOException, ResultFailException {
        final ContextInfoResponse entity = apiInterface.get(seq);
        model.addAttribute("entity", entity);
        form.setContext(entity.getContext());

        try {
            final WebVoiceResponse voice = apiInterface.getWebVoice(seq);
            ReflectionUtils.copy(form, voice);
            voice.getItems().sort(Comparator.comparingInt(WebVoiceItemsResponse::getSequence));

            voice.getItems().stream().filter(e -> Objects.equals(e.getItemType(), "HEADER")).findFirst()
                    .ifPresent(webVoiceItemsResponse -> form.setHeaderStr(webVoiceItemsResponse.getItemName()));
            voice.getItems().stream().filter(e -> Objects.equals(e.getItemType(), "TEXT")).findFirst()
                    .ifPresent(webVoiceItemsResponse -> form.setTextStr(webVoiceItemsResponse.getItemName()));

            voice.getItems().stream().filter(e -> Objects.equals(e.getItemType(), "BUTTON")).findFirst()
                    .ifPresent(webVoiceItemsResponse ->{
                        if(StringUtils.isNotEmpty(webVoiceItemsResponse.getItemName())){
                            String[] strArray = webVoiceItemsResponse.getItemName().split("");
                            for(String s : strArray){
                                switch (s) {
                                    case "B" : form.setPrev(WebVoiceItemYn.USE.getCode());
                                        break;
                                    case "F" : form.setFirst(WebVoiceItemYn.USE.getCode());
                                        break;
                                    case "C" : form.setCounseling(WebVoiceItemYn.USE.getCode());
                                        break;
                                    case "H" : form.setEnd(WebVoiceItemYn.USE.getCode());
                                        break;
                                    default: break;
                                }
                            }
                        }
                    });
            form.setTitles(
                    voice.getItems().stream().filter(e -> Objects.equals(e.getItemType(), "INPUTDIGIT")).map(e -> {
                        final WebVoiceItemsInputFormRequest inputForm = new WebVoiceItemsInputFormRequest();
                        inputForm.setInputTitle(e.getItemName());
                        inputForm.setMaxLen(e.getInputMaxLen());
                        inputForm.setIsView(e.getIsView());
                        return inputForm;
                    }).collect(Collectors.toList())
            );

            form.setDtmfs(
                    voice.getItems().stream().filter(e -> Objects.equals(e.getItemType(), "DTMF")).map(e -> {
                        final WebVoiceItemsDtmfFormRequest inputForm = new WebVoiceItemsDtmfFormRequest();
                        inputForm.setDtmfTitle(e.getItemName());
                        inputForm.setDtmfValue(e.getItemValue());
                        inputForm.setIsView(e.getIsView());
                        return inputForm;
                    }).collect(Collectors.toList())
            );
        } catch (Exception ignored) {
        }

        return "admin/service/context/modal-visual-ars";
    }
}
