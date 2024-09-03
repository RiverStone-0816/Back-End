package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

/**
 * PDS 그룹 > 속도 배수
 * ONE:1.0 배수
 * ONE_POINT_FIVE:1.5 배수
 * TWO:2.0 배수
 * TWO_POINT_FIVE:2.5 배수
 * THREE:3.0 배수
 * THREE_POINT_FIVE:3.5 배수
 * FOUR:4.0 배수
 * FOUR_POINT_FIVE:4.5 배수
 * FIVE:5.0 배수
 * FIVE_POINT_FIVE:5.5 배수
 */
public enum PDSGroupSpeedMultiple implements CodeHasable<Integer> {
    ONE(10),
    ONE_POINT_FIVE(15),
    TWO(20),
    TWO_POINT_FIVE(25),
    THREE(30),
    THREE_POINT_FIVE(35),
    FOUR(40),
    FOUR_POINT_FIVE(45),
    FIVE(50),
    FIVE_POINT_FIVE(55);

    private final Integer code;

    PDSGroupSpeedMultiple(int code) {
        this.code = code;
    }

    @Override
    public Integer getCode() {
        return code;
    }
}
