package kr.co.eicn.ippbx.server.controller.api;

import kr.co.eicn.ippbx.server.config.RequestGlobal;
import kr.co.eicn.ippbx.server.config.security.CompanyIdUsernamePasswordAuthenticationToken;
import kr.co.eicn.ippbx.server.config.security.JwtTokenProvider;
import kr.co.eicn.ippbx.server.model.UserDetails;
import kr.co.eicn.ippbx.server.model.form.LoginRequest;
import kr.co.eicn.ippbx.server.util.JsonResult;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import static kr.co.eicn.ippbx.server.util.JsonResult.Result.failure;
import static kr.co.eicn.ippbx.server.util.JsonResult.create;
import static kr.co.eicn.ippbx.server.util.JsonResult.data;

@Log4j2
@CrossOrigin
@RestController
@RequestMapping(value = "auth")
public class JwtAuthenticationController extends ApiBaseController {

	private final AuthenticationManagerBuilder authenticationManagerBuilder;
	private final JwtTokenProvider jwtTokenProvider;
	private final RequestGlobal g;

	@Autowired
	public JwtAuthenticationController(AuthenticationManagerBuilder authenticationManagerBuilder, JwtTokenProvider jwtTokenProvider, RequestGlobal g) {
		this.authenticationManagerBuilder = authenticationManagerBuilder;
		this.jwtTokenProvider = jwtTokenProvider;
		this.g = g;
	}

	@PostMapping(value = "authenticate")
	public ResponseEntity<JsonResult<String>> loginWithCreateAuthenticationToken(@Valid @RequestBody LoginRequest authenticationRequest, BindingResult bindingResult, HttpSession session) {
		if (!authenticationRequest.validate(bindingResult))
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED)
					.body(create(failure, message.getText("error.exception.cause.IllegalArgumentException"), bindingResult));

		final Authentication authenticate = authenticationManagerBuilder.getObject().authenticate(new CompanyIdUsernamePasswordAuthenticationToken(authenticationRequest, session.getId()));

		if (authenticate.isAuthenticated())
			SecurityContextHolder.getContext().setAuthentication(authenticate);

		final String token = jwtTokenProvider.generateToken((UserDetails) authenticate.getDetails());

		return ResponseEntity.ok(data(token));
	}
}
