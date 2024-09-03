package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

/**
 * PDS 그룹 > 마지막업로드상태 종류
 * DO_NOT_UPLOAD:업로드안함
 * UPLOADING:업로드중
 * COMPLETE:업로드완료
 * ERROR:업로드완료(에러)
 */
public enum PDSGroupUploadStatus implements CodeHasable<String> {
    DO_NOT_UPLOAD(""),
    UPLOADING("U"),
    COMPLETE("C"),
    ERROR("E");

    private final String code;

    PDSGroupUploadStatus(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
