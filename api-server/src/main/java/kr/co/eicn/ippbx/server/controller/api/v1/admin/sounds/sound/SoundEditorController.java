package kr.co.eicn.ippbx.server.controller.api.v1.admin.sounds.sound;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.server.exception.ValidationException;
import kr.co.eicn.ippbx.server.model.enums.TTSErrorCode;
import kr.co.eicn.ippbx.server.model.form.SoundEditorFormRequest;
import kr.co.eicn.ippbx.server.model.form.SoundEditorListenRequest;
import kr.co.eicn.ippbx.server.service.MohListService;
import kr.co.eicn.ippbx.server.service.SoundListService;
import kr.co.eicn.ippbx.server.service.StorageService;
import kr.co.eicn.ippbx.server.service.TTSService;
import kr.co.eicn.ippbx.server.util.EnumUtils;
import kr.co.eicn.ippbx.server.util.JsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Objects;

import static kr.co.eicn.ippbx.server.util.JsonResult.data;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.replace;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/sounds/editor", produces = MediaType.APPLICATION_JSON_VALUE)
public class SoundEditorController extends ApiBaseController {

	private final SoundListService soundListService;
	private final MohListService mohListService;
	private final StorageService fileSystemStorageService;

	@Value("${file.path.tts}")
	private String savePath;

	@PostMapping("make")
	public ResponseEntity<JsonResult<Integer>> make(@RequestParam String soundType, @Valid @RequestBody SoundEditorFormRequest form, BindingResult bindingResult) {
		if (!form.validate(bindingResult))
			throw new ValidationException(bindingResult);

		Integer nextSequence;

		if ("sound".equals(soundType))
			nextSequence = soundListService.created(form);
		else if ("moh".equals(soundType))
			nextSequence = mohListService.created(form);
		else
			throw new IllegalArgumentException("잘못된 형식의 파라미터입니다.");

		return ResponseEntity.created(URI.create("/api/v1/admin/sounds/editor")).body(data(nextSequence));
	}

	@PostMapping("pre-listen")
	public ResponseEntity<JsonResult<URI>> preListen(@Valid @RequestBody SoundEditorListenRequest form, BindingResult bindingResult) {
		if (!form.validate(bindingResult))
			throw new ValidationException(bindingResult);

		final String fileName = "demo_" + g.getUser().getId() + ".wav";
		final Integer ret = TTSService.createTemporaryFile(Paths.get(replace(savePath, "{0}", g.getUser().getCompanyId())).toString(), fileName, form.getComment(), form.getPlaySpeed());
		if (ret < 0)
			throw new IllegalArgumentException(String.format("음원 만들기 에러 (%s)", message.getEnumText(Objects.requireNonNull(EnumUtils.of(TTSErrorCode.class, ret)))));

		return ResponseEntity.ok(data(URI.create("/api/v1/admin/sounds/editor/resource?fileName=".concat(fileName))));
	}

	@GetMapping(value = "resource", params = {"token"})
	public ResponseEntity<Resource> resource(@RequestParam String fileName) {
		if (isEmpty(fileName))
			throw new IllegalArgumentException();

		final Resource resource = this.fileSystemStorageService.loadAsResource(replace(savePath, "{0}", g.getUser().getCompanyId()), fileName);

		return ResponseEntity.ok()
				.contentType(MediaTypeFactory.getMediaType(resource).isPresent() ? MediaTypeFactory.getMediaType(resource).get() : MediaType.APPLICATION_OCTET_STREAM)
				.headers(header -> header.add(HttpHeaders.CONTENT_DISPOSITION,
						ContentDisposition.builder("attachment").filename(Objects.requireNonNull(resource.getFilename()), StandardCharsets.UTF_8).build().toString()))
				.body(resource);
	}
}
