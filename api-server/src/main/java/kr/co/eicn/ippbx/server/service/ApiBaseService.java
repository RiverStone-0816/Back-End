package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.server.config.RequestGlobal;
import kr.co.eicn.ippbx.server.config.RequestMessage;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class ApiBaseService {
	@Autowired
	protected RequestMessage message;
	@Autowired
	protected ModelMapper modelMapper;
	@Autowired
	protected RequestGlobal g;

	protected <T, ENTITY> T convertDto(ENTITY entity, Class<T> convertType) {
		return modelMapper.map(entity, convertType);
	}
}
