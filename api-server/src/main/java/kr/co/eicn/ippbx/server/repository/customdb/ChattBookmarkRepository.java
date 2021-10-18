package kr.co.eicn.ippbx.server.repository.customdb;

import kr.co.eicn.ippbx.meta.jooq.customdb.tables.CommonChattBookmark;
import kr.co.eicn.ippbx.model.form.ChattingMemberFormRequest;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Getter
public class ChattBookmarkRepository extends CustomDBBaseRepository<CommonChattBookmark, kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonChattBookmark, Integer> {
    protected final Logger logger = LoggerFactory.getLogger(ChattBookmarkRepository.class);

    private final CommonChattBookmark TABLE;

    public ChattBookmarkRepository(String table) {
        super(new CommonChattBookmark(table), new CommonChattBookmark(table).SEQ, kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonChattBookmark.class);
        this.TABLE = new CommonChattBookmark(table);

        addField(TABLE);
    }

    public void updateBookMark(ChattingMemberFormRequest form) {
        delete(TABLE.USERID.eq(g.getUser().getId()));
        final kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonChattBookmark bookmark = new kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonChattBookmark();
        bookmark.setUserid(g.getUser().getId());
        bookmark.setMemberid(getCompanyId());

        for (String member : form.getMemberList()) {
            bookmark.setMemberid(member);
            super.insert(bookmark);
        }
    }

    public List<kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonChattBookmark> findAllByUserId() {
        return findAll(TABLE.USERID.eq(g.getUser().getId()));
    }
}
