package kr.co.eicn.ippbx.front.controller.web.admin.sounds.sounds;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.sounds.sounds.SoundsEditorApiInterface;
import kr.co.eicn.ippbx.server.model.form.SoundEditorFormRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

/**
 * @author tinywind
 */
@LoginRequired
@Controller
@RequestMapping("admin/sounds/sounds/editor")
public class SoundsEditorController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(SoundsEditorController.class);

    @Autowired
    private SoundsEditorApiInterface apiInterface;

    @GetMapping("")
    public String page(Model model, @ModelAttribute("form") SoundEditorFormRequest form) throws IOException, ResultFailException {
        return "admin/sounds/sounds/editor/ground";
    }
}
