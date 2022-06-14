package kr.co.eicn.ippbx.model;

import kr.co.eicn.ippbx.model.entity.eicn.CompanyEntity;
import kr.co.eicn.ippbx.model.enums.DataSearchAuthorityType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.sql.Timestamp;
import java.util.Collection;

@EqualsAndHashCode(callSuper = true)
@Getter @Setter
public class UserDetails extends User {
	private String    id;
	private String    idType;
	private String    passwd;
	private String    idName;
	private String    groupCode;
	private String    groupTreeName;
	private Integer   groupLevel;
	private String    isLogin;
	private String    isLoginMsg;
	private String    isLoginChatt;
	private String    extension;
	private String    peer;
	private String    phoneStatus;
	private String    etc;
	private DataSearchAuthorityType dataSearchAuthorityType;
	private Byte      logoutStatus;
	private Byte      dialStatus;
	private String    companyId;
	private String    idStatus;
	private String    hpNumber;
	private String    emailInfo;
	private Timestamp passChangeDate;
	private String    passReset;
	private String    isPds;
	private String    isStat;
	private String    isTalk;
	private String    isEmail;
	private String remoteIp;
	private String	phoneKind;

	private CompanyEntity company;

	public UserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
	}

	public UserDetails(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
	}
}
