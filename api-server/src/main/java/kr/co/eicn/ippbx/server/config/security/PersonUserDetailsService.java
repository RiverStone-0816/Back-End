package kr.co.eicn.ippbx.server.config.security;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CompanyInfo;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PersonList;
import kr.co.eicn.ippbx.model.UserDetails;
import kr.co.eicn.ippbx.model.entity.eicn.CompanyEntity;
import kr.co.eicn.ippbx.model.enums.IdType;
import kr.co.eicn.ippbx.server.repository.eicn.CompanyInfoRepository;
import kr.co.eicn.ippbx.server.repository.eicn.PersonListRepository;
import kr.co.eicn.ippbx.util.EnumUtils;
import kr.co.eicn.ippbx.util.ReflectionUtils;
import kr.co.eicn.ippbx.util.spring.RequestMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Objects;

@RequiredArgsConstructor
@Slf4j
@Service
@Primary
public class PersonUserDetailsService implements UserDetailsService {
    @Qualifier
    private final RequestMessage message;
    private final PersonListRepository repository;
    private final CompanyInfoRepository companyInfoRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        return loadUserByUsername(null, id);
    }

    public UserDetails loadUserByUsername(String companyId, String id) throws UsernameNotFoundException {
        UserDetails details;

        final PersonList person = repository.findOneById(id);
        if (person == null)
            throw new UsernameNotFoundException(message.getText("error.access.notexit"));
        if (!Objects.equals(IdType.MASTER, IdType.of(person.getIdType()))) {
            if (!person.getCompanyId().equals(companyId)) {
                throw new IllegalStateException("아이디 패스워드 인증에 실패하셨습니다.");
            }
        }

        details = new UserDetails(person.getId(), "{noop}" + person.getPasswd(),
                Collections.singletonList(EnumUtils.of(IdType.class, person.getIdType())));

        final CompanyInfo company = companyInfoRepository.findOneIfNullThrow(companyId);
        ReflectionUtils.copy(details, person);

        details.setCompanyId(company.getCompanyId());
        details.setCompany(modelMapper.map(company, CompanyEntity.class));

        return details;
    }
}
