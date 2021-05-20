package kr.co.eicn.ippbx.server.config;

import kr.co.eicn.ippbx.model.UserDetails;
import kr.co.eicn.ippbx.model.enums.IdType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Slf4j
@Component
public class RequestGlobal {
	public UserDetails getUser() {
		final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			if (!(auth instanceof AnonymousAuthenticationToken)) {
				return (UserDetails) auth.getDetails();
			}
		}

		return null;
	}

	public boolean isLogin() {
		return getUser() != null;
	}

	public boolean isAdmin() {
		return isLogin() && IdType.isAdmin(getUser().getIdType());
	}

	public boolean isServiceAvailable(final String service) {
		return isLogin() && getUser().getCompany() != null && Stream.of(getUser().getCompany().getService()).anyMatch(e -> e.equals(service));
	}
}
