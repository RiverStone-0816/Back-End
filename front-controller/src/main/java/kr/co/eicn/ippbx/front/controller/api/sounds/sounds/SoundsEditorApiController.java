package kr.co.eicn.ippbx.front.controller.api.sounds.sounds;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.sounds.sounds.SoundsEditorApiInterface;
import kr.co.eicn.ippbx.model.form.SoundEditorFormRequest;
import kr.co.eicn.ippbx.model.form.SoundEditorListenRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;

/**
 * @author tinywind
 */
@Api(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequestMapping(value = "api/sounds-editor", produces = MediaType.APPLICATION_JSON_VALUE)
public class SoundsEditorApiController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(SoundsEditorApiController.class);

    @Autowired
    private SoundsEditorApiInterface apiInterface;

    @PostMapping("make")
    public Integer make(@RequestParam String soundType, @Valid @RequestBody SoundEditorFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        return apiInterface.make(soundType, form);
    }

    @PostMapping("pre-listen")
    public URI preListen(@Valid @RequestBody SoundEditorListenRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        return apiInterface.preListen(form);
    }
}
