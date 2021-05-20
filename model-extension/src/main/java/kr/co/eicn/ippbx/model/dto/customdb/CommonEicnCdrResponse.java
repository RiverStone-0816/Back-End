package kr.co.eicn.ippbx.model.dto.customdb;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PersonList;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ServiceList;
import kr.co.eicn.ippbx.model.entity.customdb.EicnCdrEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode(callSuper = true)
@Data
public class CommonEicnCdrResponse extends EicnCdrEntity {
	private PersonList personList; // 상담원 정보
	private String grade;
	private ServiceList service; // 대표번호 정보

	@JsonIgnore
	@Override
	public int getNCallResult() {
		return super.getNCallResult();
	}
}
