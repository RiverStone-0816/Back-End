package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.GwInfo;
import kr.co.eicn.ippbx.server.model.dto.eicn.search.SearchGwInfoResponse;
import kr.co.eicn.ippbx.server.model.search.search.SearchGwInfoRequest;
import lombok.Getter;
import org.jooq.Condition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.server.jooq.eicn.tables.GwInfo.GW_INFO;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Getter
@Repository
public class GatewayRepository extends EicnBaseRepository<GwInfo, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.GwInfo, String> {
	protected final Logger logger = LoggerFactory.getLogger(GatewayRepository.class);

	public GatewayRepository() {
		super(GW_INFO, GW_INFO.HOST, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.GwInfo.class);

		orderByFields.add(GW_INFO.NAME.asc());
	}

	public List<SearchGwInfoResponse> search(SearchGwInfoRequest search) {
		return findAll(searchConditions(search)).stream()
				.map(e -> modelMapper.map(e, SearchGwInfoResponse.class))
				.collect(Collectors.toList());
	}

	private List<Condition> searchConditions(SearchGwInfoRequest search) {
		final List<Condition> conditions = new ArrayList<>();

		if (isNotEmpty(search.getHost()))
			conditions.add(GW_INFO.HOST.eq(search.getHost()));

		return conditions;
	}
}
