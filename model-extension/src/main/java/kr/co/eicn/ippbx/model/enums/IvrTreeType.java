package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

/**
 * // TODO: 삭제예정
 * ivr_tree.type
 * 0:상위 트리, 1:메뉴다시듣기, 2:연결번호로통화연결, 3:내선번호직접연결, 4:음성안내종료, 5:하위IVR로연결,
 * 6:이전단계로돌아가기, 7:처음으로돌아가기, 8:음원플레이, 9:음성사서함 10:예외처리후번호연결, 11:예외처리후다른IVR
 * 13: 헌트번호연결
 */
public enum IvrTreeType implements CodeHasable<Byte> {
	PARENT_TREE((byte)0), RE_LISTENING_TO_THE_MENU((byte) 1), CALL_CONNECTION_BY_CONNECTION_NUMBER((byte) 2), DIRECT_CONNECTION_OF_EXTENSION_NUMBERS((byte) 3),
	VOICE_GUIDANCE_EXIT((byte) 4), LINK_TO_ANOTHER_IVR((byte) 5),
	RETURN_TO_THE_PREVIOUS_STEP((byte) 6), RETURN_TO_THE_FIRST_STEP((byte) 7), RECORD_PLAY((byte) 8), VOICE_BOX((byte) 9),
	NUMBER_CONNECTION_AFTER_EXCEPTION_PROCESSING((byte) 10), ANOTHER_IVR_AFTER_EXCEPTION_PROCESSING((byte) 11),
	HUNT((byte)13);

	private Byte code;

	IvrTreeType(byte code) {
		this.code = code;
	}

	@Override
	public Byte getCode() {
		return code;
	}

	public static IvrTreeType of (byte value) {
		for (IvrTreeType type : IvrTreeType.values()){
			if (type.getCode().equals(value))
				return type;
		}

		return null;
	}
}
