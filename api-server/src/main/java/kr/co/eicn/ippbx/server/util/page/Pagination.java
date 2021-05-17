package kr.co.eicn.ippbx.server.util.page;

import java.util.List;

/**
 * @author tinywind
 * @since 2016-08-09
 */
public class Pagination<T> {
    private List<T> rows;
    private int page;
    private int totalCount;
    private int numberOfRowsPerPage;

    public Pagination() {
    }

    public Pagination(List<T> rows, int page, int totalCount, int numberOfRowsPerPage) {
        set(rows, page, totalCount, numberOfRowsPerPage);
    }

    public void set(List<T> rows, int page, int totalCount, int numberOfRowsPerPage) {
        this.rows = rows;
        this.page = page;
        this.totalCount = totalCount;
        this.numberOfRowsPerPage = numberOfRowsPerPage;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getNumberOfRowsPerPage() {
        return numberOfRowsPerPage;
    }

    public void setNumberOfRowsPerPage(int numberOfRowsPerPage) {
        this.numberOfRowsPerPage = numberOfRowsPerPage;
    }
}
