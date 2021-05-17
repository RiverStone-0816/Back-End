package kr.co.eicn.ippbx.server.util.jooq;

import kr.co.eicn.ippbx.server.config.Constants;
import kr.co.eicn.ippbx.server.util.ContextUtil;
import kr.co.eicn.ippbx.server.util.page.PageForm;
import kr.co.eicn.ippbx.server.util.page.Pagination;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.jooq.SelectLimitStep;

import java.util.List;
import java.util.stream.Collectors;

public class JooqPaginationFactory {

    private JooqPaginationFactory() {
    }

    public static <T> Pagination<T> create(SelectLimitStep<Record> query, Class<T> klass, PageForm page) {
        return create(query, record -> record.into(klass), page);
    }

    public static <T> Pagination<T> create(SelectLimitStep<Record> query, RecordMapper<Record, T> mapper, PageForm page) {
        return create(dsl(), query, mapper, page);
    }

    public static <T> Pagination<T> create(DSLContext create, SelectLimitStep<Record> query, RecordMapper<Record, T> mapper, PageForm page) {
        final List<T> rows = create.fetchStream(query.offset(page.getOffset()).limit(page.getLimit())).map(mapper::map).collect(Collectors.toList());
        final int count = create.fetchCount(query);
        return new Pagination<>(rows, page.getPage(), count, page.getLimit());
    }

    public static <T> Pagination<T> create(DSLContext create, SelectLimitStep<Record> query, SelectLimitStep<Record> countQuery, RecordMapper<Record, T> mapper, PageForm page) {
        final List<T> rows = create.fetchStream(query.offset(page.getOffset()).limit(page.getLimit())).map(mapper::map).collect(Collectors.toList());
        final int count = create.fetchCount(countQuery);
        return new Pagination<>(rows, page.getPage(), count, page.getLimit());
    }

    private static DSLContext dsl() {
        return dsl(Constants.BEAN_DSL_EICN);
    }

    private static DSLContext dsl(String beanName) {
        return (DSLContext) ContextUtil.getBean(beanName);
    }
}
