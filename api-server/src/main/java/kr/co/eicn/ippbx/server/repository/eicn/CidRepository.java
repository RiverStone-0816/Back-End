package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.CidInfo;
import kr.co.eicn.ippbx.model.dto.eicn.search.SearchCidResponse;
import kr.co.eicn.ippbx.model.search.search.SearchCidRequest;
import lombok.Getter;
import org.jooq.Condition;
import org.jooq.types.UInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.CidInfo.CID_INFO;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Getter
@Repository
public class CidRepository extends EicnBaseRepository<CidInfo, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CidInfo, UInteger> {
	protected final Logger logger = LoggerFactory.getLogger(CidRepository.class);

	public CidRepository() {
		super(CID_INFO, CID_INFO.SEQ, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CidInfo.class);

		orderByFields.add(CID_INFO.CID_NUMBER.asc());
	}

	public List<SearchCidResponse> search(SearchCidRequest search) {
		return findAll(searchConditions(search)).stream()
				.map(e -> modelMapper.map(e, SearchCidResponse.class))
				.collect(Collectors.toList());
	}

	private List<Condition> searchConditions(SearchCidRequest search) {
		final List<Condition> conditions = new ArrayList<>();

		if (isNotEmpty(search.getCidNumber()))
			conditions.add(CID_INFO.CID_NUMBER.eq(search.getCidNumber()));

		return conditions;
	}
}
