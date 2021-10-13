package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

public enum TalkTemplateTypeMent implements CodeHasable<String> {
    TEXT("T"), PHOTO("P");

    private final String typeMent;

    TalkTemplateTypeMent(String code) {
        this.typeMent = code;
    }

    @Override
    public String getCode() {
        return this.typeMent;
    }

    public static TalkTemplateTypeMent of (String value) {
        for (TalkTemplateTypeMent typeMent : TalkTemplateTypeMent.values()){
            if (typeMent.getCode().equals(value))
                return typeMent;
        }

        return null;
    }
}
