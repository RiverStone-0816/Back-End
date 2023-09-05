package kr.co.eicn.ippbx.server.controller.api;

import kr.co.eicn.ippbx.exception.ValidationException;
import kr.co.eicn.ippbx.model.form.PersonMePasswordUpdateRequest;
import kr.co.eicn.ippbx.model.form.PersonPasswordUpdateRequest;
import kr.co.eicn.ippbx.server.repository.eicn.UserRepository;
import kr.co.eicn.ippbx.util.JsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static kr.co.eicn.ippbx.util.JsonResult.create;

/**
 * 로그인 유저
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/me", produces = MediaType.APPLICATION_JSON_VALUE)
public class MeApiController extends ApiBaseController {

	private final UserRepository userRepository;

	/**
	 * 사용자 비밀번호 갱신
	 */
	@PatchMapping("password")
	public ResponseEntity<JsonResult<Void>> updatePassword(@Valid @RequestBody PersonMePasswordUpdateRequest form, BindingResult bindingResult) {
		if (!form.validate(bindingResult))
			throw new ValidationException(bindingResult);

		if(userRepository.checkOldPassword(g.getUser().getId(), form.getOldPassword()) == 0)
			throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");

		userRepository.updatePassword(g.getUser().getId(), form.getPassword());
		return ResponseEntity.ok(create());
	}
}
