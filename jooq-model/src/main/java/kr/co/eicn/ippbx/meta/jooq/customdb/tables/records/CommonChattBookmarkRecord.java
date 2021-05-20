package kr.co.eicn.ippbx.meta.jooq.customdb.tables.records;

import org.jooq.Table;
import org.jooq.impl.TableRecordImpl;

public class CommonChattBookmarkRecord extends TableRecordImpl<CommonChattBookmarkRecord> {

    public CommonChattBookmarkRecord(Table<CommonChattBookmarkRecord> table) {
        super(table);
    }

    /**
     * Setter for <code>CUSTOMDB.chatt_bookmark.seq</code>.
     */
    public void setSeq(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>CUSTOMDB.chatt_bookmark.seq</code>.
     */
    public Integer getSeq() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>CUSTOMDB.chatt_bookmark.userid</code>.
     */
    public void setUserid(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>CUSTOMDB.chatt_bookmark.userid</code>.
     */
    public String getUserid() {
        return (String) get(1);
    }

    /**
     * Setter for <code>CUSTOMDB.chatt_bookmark.memberid</code>.
     */
    public void setMemberid(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>CUSTOMDB.chatt_bookmark.memberid</code>.
     */
    public String getMemberid() {
        return (String) get(2);
    }
}
