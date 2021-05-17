package kr.co.eicn.ippbx.server.model.enums;

/**
 * 채널 구분
 * 상담톡, 콜백, 이관, 예약, 프리뷰, 이메일, 게시판, 전화번호
 */
public enum ChannelType {
	TALK, CALLBACK, TRANSFER, RESERVATION, PREVIEW, EMAIL, NOTICE, PHONE;

	public static ChannelType of(String channelType) {
		for (ChannelType value : ChannelType.values()) {
			if (value.name().equals(channelType))
				return value;
		}
		return null;
	}
}
