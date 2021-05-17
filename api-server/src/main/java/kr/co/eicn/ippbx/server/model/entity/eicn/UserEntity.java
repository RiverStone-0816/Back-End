package kr.co.eicn.ippbx.server.model.entity.eicn;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.CompanyTree;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.PersonList;
import kr.co.eicn.ippbx.server.model.enums.RecordingActionType;
import kr.co.eicn.ippbx.server.model.enums.RecordingAuthorityType;
import kr.co.eicn.ippbx.server.util.EnumUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isEmpty;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserEntity extends PersonList {
	private CompanyEntity company;      // 상담원 고객사 정보
	private CompanyTree companyTree;    // 조직정보
	private String listeningRecordingAuthority; // 녹취권한 듣기
	private String downloadRecordingAuthority; // 녹취권한 다운
	private String removeRecordingAuthority; // 녹취권한 삭제

	private List<CompanyTree> companyTrees; // 조직 정보

	public String getListeningRecordingAuthority() {
		return recordingAuthorityValue(RecordingActionType.LISTENING) != null ? recordingAuthorityValue(RecordingActionType.LISTENING).getCode() : null;
	}

	public String getDownloadRecordingAuthority() {
		return recordingAuthorityValue(RecordingActionType.DOWNLOAD) != null ? recordingAuthorityValue(RecordingActionType.DOWNLOAD).getCode() : null;
	}

	public String getRemoveRecordingAuthority() {
		return recordingAuthorityValue(RecordingActionType.REMOVE) != null ? recordingAuthorityValue(RecordingActionType.REMOVE).getCode() : null;
	}

	public RecordingAuthorityType recordingAuthorityValue(RecordingActionType actionType) {
		if (isEmpty(super.getEtc()))
			return null;

		for (String recordingAuthorityType : super.getEtc().split("\\|")) {
			if (recordingAuthorityType.contains(actionType.getCode().concat(":"))) {
				return EnumUtils.of(RecordingAuthorityType.class, recordingAuthorityType.split(":")[1]);
			}
		}
		return null;
	}
}
