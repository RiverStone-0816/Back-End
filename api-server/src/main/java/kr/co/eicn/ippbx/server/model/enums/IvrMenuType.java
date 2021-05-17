package kr.co.eicn.ippbx.server.model.enums;

import kr.co.eicn.ippbx.server.util.CodeHasable;

/**
 * ivr_tree.type
 * 1:메뉴연결,
 * 2:대표번호연결,
 * 3:헌트번호연결,
 * 4:내선번호연결,
 * 5:외부번호로연결
 * 6:예외처리후종료,
 * 7:예외처리후메뉴연결,
 * 8:예외처리후대표번호연결,
 * 9:예외처리후헌트번호연결
 * 10:내선직접연결
 *
 * 20:메뉴다시듣기
 * 21:음원연결후종료
 * 22:이전메뉴
 * 23:처음단계
 */
public enum IvrMenuType implements CodeHasable<Byte> {
    CONNECT_MENU((byte) 1, true, true, ImageType.CONNECT),
    CONNECT_REPRESENTABLE_NUMBER((byte) 2, false, false, ImageType.CONNECT),
    CONNECT_HUNT_NUMBER((byte) 3, false, false, ImageType.CONNECT),
    CONNECT_INNER_NUMBER((byte) 4, false, false, ImageType.CONNECT),
    CONNECT_OUTER_NUMBER((byte) 5, false, false, ImageType.CONNECT),
    FINISH_AFTER_DONE_EXCEPTION((byte) 6, false, false, ImageType.FINISH),
    CONNECT_MENU_AFTER_DONE_EXCEPTION((byte) 7, false, true, ImageType.EXCEPTION),
    CONNECT_REPRESENTABLE_NUMBER_AFTER_DONE_EXCEPTION((byte) 8, false, false, ImageType.EXCEPTION),
    CONNECT_HUNT_NUMBER_AFTER_DONE_EXCEPTION((byte) 9, false, false, ImageType.EXCEPTION),
    CONNECT_INNER_NUMBER_DIRECTLY((byte) 10, false, false, ImageType.CONNECT),
    RETRY_MENU((byte) 20, false, false, ImageType.SOUND),
    FINISH_AFTER_CONNECT_SOUND((byte) 21, false, false, ImageType.SOUND),
    TO_PREVIOUS_MENU((byte) 22, false, false, ImageType.MOVE),
    TO_FIRST_MENU((byte) 23, false, false, ImageType.MOVE);

    private final byte code;
    private final boolean rootable;
    private final boolean menu;
    private final ImageType imageType;

    IvrMenuType(byte code, boolean rootable, boolean menu, ImageType imageType) {
        this.code = code;
        this.rootable = rootable;
        this.menu = menu;
        this.imageType = imageType;
    }

    @Override
    public Byte getCode() {
        return code;
    }

    public boolean isRootable() {
        return rootable;
    }

    public boolean isMenu() {
        return menu;
    }

    public ImageType getImageType() {
        return imageType;
    }

    public static IvrMenuType of (byte value) {
        for (IvrMenuType type : IvrMenuType.values()){
            if (type.getCode().equals(value))
                return type;
        }

        return null;
    }
}

enum ImageType {
    CONNECT, FINISH, MOVE, EXCEPTION, SOUND
}