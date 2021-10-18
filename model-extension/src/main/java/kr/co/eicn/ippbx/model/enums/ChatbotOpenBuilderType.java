package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

public enum ChatbotOpenBuilderType implements CodeHasable<String> {
    BUILD("BD"), SKILL("SK");

    private final String code;

    ChatbotOpenBuilderType(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return this.code;
    }
}
