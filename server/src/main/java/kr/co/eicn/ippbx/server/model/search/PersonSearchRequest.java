package kr.co.eicn.ippbx.server.model.search;

import kr.co.eicn.ippbx.server.model.enums.IdStatus;
import kr.co.eicn.ippbx.server.util.SortField;
import kr.co.eicn.ippbx.server.util.page.PageForm;
import kr.co.eicn.ippbx.server.util.page.PageQueryable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PersonSearchRequest extends PageForm {
    @PageQueryable
    private String id;              // 아이디
    @PageQueryable
    private String idName;          // 성명
    @PageQueryable
    private String extension;       // 내선
    @PageQueryable
    private String groupCode;       // 조직코드
    @PageQueryable
    private Sort sort;            // 정렬 필드
    @PageQueryable
    private IdStatus idStatus;      // 근무상태("": 정상근무, S:휴직, X:퇴직)

    public enum Sort implements SortField {
        NAME("id_name"), GROUP("group_name"), PDS("is_pds"), STAT("is_stat"),
        TALK("is_talk");

        private final String field;

        Sort(String field) {
            this.field = field;
        }

        @Override
        public String field() {
            return this.field;
        }
    }
}
