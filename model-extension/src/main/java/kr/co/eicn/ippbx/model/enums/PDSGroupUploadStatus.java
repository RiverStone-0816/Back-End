package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

/**
 * pds_group.last_upload_status
 * "":업로드안함, U:업로드중, C:업로드완료, E: 업로드완료(에러:)
 */
public enum PDSGroupUploadStatus implements CodeHasable<String> {
	DO_NOT_UPLOAD(""), UPLOADING("U"), COMPLETE("C"), ERROR("E");

	private String code;

	PDSGroupUploadStatus(String code) {
		this.code = code;
	}

	@Override
	public String getCode() {
		return code;
	}
}
