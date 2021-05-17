package kr.co.eicn.ippbx.server.model.enums;

import kr.co.eicn.ippbx.server.util.CodeHasable;

/**
 *  파일 업로드 상태
 */
public enum UploadStatus implements CodeHasable<String> {
    NONE("N"), UPLOAD("C"), UPLOADING("U"), UPLOAD_ERROR("E");

    private String code;

    UploadStatus(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
