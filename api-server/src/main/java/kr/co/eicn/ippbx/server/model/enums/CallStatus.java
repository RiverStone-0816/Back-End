package kr.co.eicn.ippbx.server.model.enums;

import kr.co.eicn.ippbx.server.util.CodeHasable;

/**
 * 호상태
 * 16: 정상, 17: 비수신, 19: 통화중, 0: 실패, 330: 기타
 */
public enum CallStatus implements CodeHasable<String> {
	fail("0"),
	unallocated("1"),
	interworking_unspecified("127"),
	normal_clear("16"),
	user_busy("17"),
	no_user_responding("18"),
	no_answer("19"),
	subscriber_absent("20"),
	call_rejected("21"),
	number_changed("22"),
	answered_elsewhere("26"),
	destination_out_of_order("27"),
	invalid_number_format("28"),
	facility_rejected("29"),
	circuit_channel_congestion("34"),
	network_out_of_order("38"),
	bearer_capability_not_available("58"),
	pds("220"),
	vm("221"),
	fax("222"),
	ars("223"),
	local_forward("330");

	private String code;

	CallStatus(String code) {
		this.code = code;
	}

	@Override
	public String getCode() {
		return code;
	}
}
