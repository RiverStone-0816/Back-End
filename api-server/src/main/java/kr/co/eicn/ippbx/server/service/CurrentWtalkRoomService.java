package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.model.entity.customdb.TalkRoomEntity;
import kr.co.eicn.ippbx.model.form.TalkCurrentListSearchRequest;
import kr.co.eicn.ippbx.model.search.TalkRoomSearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.CurrentWtalkRoomRepository;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.AllArgsConstructor;
import org.jooq.tools.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class CurrentWtalkRoomService extends ApiBaseService {
    private final CurrentWtalkRoomRepository currentTalkRoomRepository;

    public Pagination<TalkRoomEntity> pagination(TalkRoomSearchRequest search) {
        return currentTalkRoomRepository.pagination(search);
    }

    public List<TalkRoomEntity> findAll(TalkCurrentListSearchRequest search) {
        List<TalkRoomEntity> list = currentTalkRoomRepository.findAll(search);

        return filter(list, search.getMode());
    }

    public List<TalkRoomEntity> filter(List<TalkRoomEntity> list, String mode) {
        return list.stream().filter(e -> StringUtils.isEmpty(mode) || e.getTalkRoomMode().getCode().equals(mode)).collect(Collectors.toList());
    }
}
