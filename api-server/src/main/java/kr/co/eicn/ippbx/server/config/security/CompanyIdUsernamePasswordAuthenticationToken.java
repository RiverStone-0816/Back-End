package kr.co.eicn.ippbx.server.config.security;

import kr.co.eicn.ippbx.model.UserDetails;
import kr.co.eicn.ippbx.model.form.LoginRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class CompanyIdUsernamePasswordAuthenticationToken extends UsernamePasswordAuthenticationToken {
	private String companyId;
	private String extension;
	private String sessionId;
	private String actionType;
	private Boolean isChatLogin;

	public CompanyIdUsernamePasswordAuthenticationToken(LoginRequest authenticationRequest) {
		super(authenticationRequest.getId(), authenticationRequest.getPassword());
		this.companyId = authenticationRequest.getCompany();
		this.extension = authenticationRequest.getExtension();
		this.actionType = authenticationRequest.getActionType();
		this.isChatLogin = authenticationRequest.getIsChatLogin();
	}

	public CompanyIdUsernamePasswordAuthenticationToken(LoginRequest authenticationRequest, String sessionId) {
		super(authenticationRequest.getId(), authenticationRequest.getPassword());
		this.companyId = authenticationRequest.getCompany();
		this.extension = authenticationRequest.getExtension();
		this.sessionId = sessionId;
		this.actionType = authenticationRequest.getActionType();
		this.isChatLogin = authenticationRequest.getIsChatLogin();
	}

	public CompanyIdUsernamePasswordAuthenticationToken(UserDetails userDetails) {
		super(userDetails.getId(), userDetails.getPassword(), userDetails.getAuthorities());
		this.companyId = userDetails.getCompanyId();
		this.extension = userDetails.getExtension();
		this.setDetails(userDetails);
	}

	public String getCompanyId() {
		return this.companyId;
	}

	public String getExtension() {
		return extension;
	}

	public String getSessionId() {
		return sessionId;
	}

	public String getActionType() { return actionType; }

	public Boolean getIsChatLogin() { return isChatLogin; }
}
