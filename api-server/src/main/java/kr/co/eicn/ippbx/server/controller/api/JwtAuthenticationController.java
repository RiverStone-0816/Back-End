package kr.co.eicn.ippbx.server.controller.api;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PersonList;
import kr.co.eicn.ippbx.model.UserDetails;
import kr.co.eicn.ippbx.model.dto.eicn.PersonListSummary;
import kr.co.eicn.ippbx.model.form.LoginRequest;
import kr.co.eicn.ippbx.server.config.RequestGlobal;
import kr.co.eicn.ippbx.server.config.security.CompanyIdUsernamePasswordAuthenticationToken;
import kr.co.eicn.ippbx.server.config.security.JwtTokenProvider;
import kr.co.eicn.ippbx.server.repository.eicn.PersonListRepository;
import kr.co.eicn.ippbx.util.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.jooq.tools.StringUtils;
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

import static kr.co.eicn.ippbx.util.JsonResult.Result.failure;
import static kr.co.eicn.ippbx.util.JsonResult.create;
import static kr.co.eicn.ippbx.util.JsonResult.data;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping(value = "auth")
public class JwtAuthenticationController extends ApiBaseController {

	private final AuthenticationManagerBuilder authenticationManagerBuilder;
	private final JwtTokenProvider jwtTokenProvider;
	private final PersonListRepository personListRepository;
	private final RequestGlobal g;

	@Autowired
	public JwtAuthenticationController(AuthenticationManagerBuilder authenticationManagerBuilder, JwtTokenProvider jwtTokenProvider, PersonListRepository personListRepository, RequestGlobal g) {
		this.authenticationManagerBuilder = authenticationManagerBuilder;
		this.jwtTokenProvider = jwtTokenProvider;
		this.personListRepository = personListRepository;
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

	@PostMapping(value = "mc-only")
	public ResponseEntity<JsonResult<String>> loginWithCreateAuthenticationToken(@RequestBody PersonListSummary data, HttpSession session) {
		if (StringUtils.isEmpty(data.getId()))
			throw new IllegalArgumentException();

		final PersonList user = personListRepository.findOneById(data.getId());

		LoginRequest loginRequest = new LoginRequest();

		loginRequest.setId(user.getId());
		loginRequest.setExtension(user.getExtension());
		loginRequest.setPassword(user.getPasswd());
		loginRequest.setCompany(data.getCompanyId());

		final Authentication authenticate = authenticationManagerBuilder.getObject().authenticate(new CompanyIdUsernamePasswordAuthenticationToken(loginRequest, session.getId()));

		if (authenticate.isAuthenticated())
			SecurityContextHolder.getContext().setAuthentication(authenticate);

		final String token = jwtTokenProvider.generateToken((UserDetails) authenticate.getDetails());

		return ResponseEntity.ok(data(token));
	}
}
