package kr.co.eicn.ippbx.server.repository;

import kr.co.eicn.ippbx.exception.DuplicateKeyException;
import kr.co.eicn.ippbx.exception.EntityNotFoundException;
import kr.co.eicn.ippbx.server.config.RequestGlobal;
import kr.co.eicn.ippbx.util.jooq.JooqPaginationFactory;
import kr.co.eicn.ippbx.util.page.PageForm;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.util.spring.RequestMessage;
import lombok.Data;
import org.jooq.Record;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Data
@Repository
public abstract class BaseRepository<TABLE extends TableImpl<? extends Record>, ENTITY, PK> {
    protected final Set<Field<?>> selectingFields = new HashSet<>();
    protected final List<SortField<?>> orderByFields = new ArrayList<>();

    @Autowired(required = false)
    protected RequestGlobal g;
    @Autowired(required = false)
    protected RequestMessage message;
    @Autowired(required = false)
    protected ModelMapper modelMapper;

    protected Table<?> table;
    protected Class<ENTITY> entityClass;

    public BaseRepository(TABLE table, Class<ENTITY> entityClass) {
        this.table = table;
        this.entityClass = entityClass;
    }

    protected abstract DSLContext dsl();

    protected abstract Logger getLogger();

    protected void addField(Field<?>... fields) {
        selectingFields.addAll(Arrays.asList(fields));
    }

    protected void addField(TableImpl<?> table) {
        addField(table.fields());
    }

    protected void addOrderingField(SortField<?>... fields) {
        orderByFields.addAll(Arrays.asList(fields));
    }

    protected SelectJoinStep<Record> query() {
        return query(dsl());
    }

    protected SelectJoinStep<Record> query(DSLContext dsl) {
        return dsl.select(selectingFields)
                .from(table);
    }

    /**
     * Join query 존재시 Override 해야 한다.
     */
    protected SelectConditionStep<Record> query(SelectJoinStep<Record> query) {
        return query.where();
    }

    /**
     * 후처리. getMapper() 후 적용
     */
    protected void postProcedure(List<ENTITY> entities) {
    }

    /**
     * Join query 존재시 Override 해야 한다.
     */
    protected RecordMapper<Record, ENTITY> getMapper() {
        return record -> record.into((Table<?>) table).into(entityClass);
    }

    protected abstract Condition getCondition(PK key);

    public boolean existsTable() {
        return dsl().selectCount()
                .from("information_schema.tables")
                .where("table_name = '" + getTable().getName() + "'")
                .fetchOne().value1() > 0;
    }

    public int dropTableIfExists() {
        return dropTableIfExists(dsl());
    }

    public int dropTableIfExists(DSLContext dslContext) {
        return dslContext.dropTableIfExists(table).execute();
    }

    public ENTITY findOne(PK key) {
        return findOne(getCondition(key));
    }

    public ENTITY findOne(Condition condition) {
        return findOne(getCompanyId(), condition);
    }

    public ENTITY findOne(String companyId, PK key) {
        return findOne(companyId, getCondition(key));
    }

    public ENTITY findOne(String companyId, Condition condition) {
        return findOne(dsl(), companyId, condition);
    }

    public ENTITY findOne(DSLContext dslContext, String companyId, Condition condition) {
        final SelectHavingStep<Record> query = query(query(dslContext))
                .and(condition)
                .and(compareCompanyId(companyId));

        if (orderByFields.size() > 0)
            query.orderBy(orderByFields);

        final ENTITY entity = query.fetchOne(getMapper());

        if (entity != null)
            postProcedure(Collections.singletonList(entity));

        return entity;
    }

    public ENTITY findOneIfNullThrow(PK key) {
        return findOneIfNullThrow(getCondition(key));
    }

    public ENTITY findOneIfNullThrow(Condition condition) {
        return findOneIfNullThrow(getCompanyId(), condition);
    }


    public ENTITY findOneIfNullThrow(String companyId, PK key) {
        return findOneIfNullThrow(companyId, getCondition(key));
    }

    public ENTITY findOneIfNullThrow(String companyId, Condition condition) {
        final ENTITY entity = findOne(companyId, condition);
        if (entity == null) {
            getLogger().info("존재하지 않는 정보입니다: {} [{}]", table.getName(), condition.toString());
            throw new EntityNotFoundException(String.format("존재하지 않는 정보입니다.(%s)", table.getComment()));
        }
        return entity;
    }

    public ENTITY findOneIfNullThrow(DSLContext dsl, PK key) {
        return findOneIfNullThrow(dsl, getCondition(key));
    }

    public ENTITY findOneIfNullThrow(DSLContext dsl, Condition condition) {
        return findOneIfNullThrow(dsl, getCompanyId(), condition);
    }


    public ENTITY findOneIfNullThrow(DSLContext dsl, String companyId, PK key) {
        return findOneIfNullThrow(dsl, companyId, getCondition(key));
    }

    public ENTITY findOneIfNullThrow(DSLContext dsl, String companyId, Condition condition) {
        final ENTITY entity = findOne(dsl, companyId, condition);
        if (entity == null) {
            getLogger().info("존재하지 않는 정보입니다: {} [{}]", table.getName(), condition.toString());
            throw new EntityNotFoundException(String.format("존재하지 않는 정보입니다.(%s)", table.getComment()));
        }
        return entity;
    }

    public List<ENTITY> findAll() {
        return findAll(dsl(), DSL.trueCondition());
    }

    public List<ENTITY> findAll(Condition condition) {
        return findAll(dsl(), condition);
    }

    public List<ENTITY> findAll(DSLContext dsl, Condition condition) {
        return findAll(dsl, Collections.singletonList(condition));
    }

    public List<ENTITY> findAll(Condition condition, SortField<?> orderField) {
        return findAll(dsl(), Collections.singletonList(condition), Collections.singletonList(orderField));
    }

    public List<ENTITY> findAll(Condition condition, List<SortField<?>> orderByFields) {
        return findAll(dsl(), Collections.singletonList(condition), orderByFields);
    }

    public List<ENTITY> findAll(List<Condition> conditions) {
        return findAll(dsl(), conditions);
    }

    public List<ENTITY> findAll(List<Condition> conditions, List<SortField<?>> orderByFields) {
        return findAll(dsl(), conditions, orderByFields);
    }

    public List<ENTITY> findAll(DSLContext dsl, List<Condition> conditions) {
        return findAll(dsl, conditions, orderByFields);
    }

    public List<ENTITY> findAll(DSLContext dsl, Condition condition, List<SortField<?>> orderByFields) {
        return findAll(dsl, Collections.singletonList(condition), orderByFields);
    }

    public List<ENTITY> findAll(DSLContext dsl, List<Condition> conditions, List<SortField<?>> orderByFields) {
        final SelectConditionStep<Record> query = query(query(dsl))
                .and(compareCompanyId());

        for (Condition e : conditions)
            query.and(e);

        if (orderByFields.size() > 0)
            query.orderBy(orderByFields);

        final List<ENTITY> entities = query.fetch(getMapper());
        postProcedure(entities);
        return entities;
    }

    public Pagination<ENTITY> pagination(PageForm search) {
        return pagination(dsl(), search);
    }

    public Pagination<ENTITY> pagination(DSLContext dsl, PageForm search) {
        final SelectHavingStep<Record> query = pagingQuery(query());

        if (Objects.nonNull(search.getSort()))
            query.orderBy(DSL.field(DSL.name(search.getSort().field())).sort(Objects.isNull(search.getOrderDirection()) ? SortOrder.ASC : search.getOrderDirection()));

        if (orderByFields.size() > 0)
            query.orderBy(orderByFields);

        final Pagination<ENTITY> pagination = JooqPaginationFactory.create(dsl,
                query,
                fetchCount(dsl.select(Collections.singletonList(table.field(0))).from(table)),
                getMapper(), search);
        postProcedure(pagination.getRows());
        return pagination;
    }

    public Pagination<ENTITY> pagination(PageForm search, Condition... conditions) {
        return pagination(search, Arrays.asList(conditions));
    }

    public Pagination<ENTITY> pagination(PageForm search, List<Condition> conditions) {
        return pagination(search, conditions, orderByFields);
    }

    public Pagination<ENTITY> pagination(PageForm search, List<Condition> conditions, List<SortField<?>> orderByFields) {
        return pagination(dsl(), search, conditions, orderByFields);
    }

    public Pagination<ENTITY> pagination(DSLContext dsl, PageForm search, List<Condition> conditions, List<SortField<?>> orderByFields) {
        final SelectHavingStep<Record> query = pagingQuery(query(), conditions);

        if (Objects.nonNull(search.getSort()))
            query.orderBy(DSL.field(DSL.name(search.getSort().field())).sort(Objects.isNull(search.getOrderDirection()) ? SortOrder.ASC : search.getOrderDirection()));

        if (orderByFields.size() > 0)
            query.orderBy(orderByFields);

        final Pagination<ENTITY> pagination = JooqPaginationFactory.create(dsl,
                query,
                getMapper(), search,
                fetchCount(dsl.select(Collections.singletonList(table.field(0))).from(table), conditions).fetch().size());
        postProcedure(pagination.getRows());
        return pagination;
    }

    private SelectHavingStep<Record> pagingQuery(SelectJoinStep<Record> joinQuery) {
        return pagingQuery(joinQuery, Collections.emptyList());
    }

    private SelectHavingStep<Record> pagingQuery(SelectJoinStep<Record> joinQuery, List<Condition> conditions) {
        final SelectConditionStep<Record> query = query(joinQuery)
                .and(compareCompanyId());

        for (Condition e : conditions)
            query.and(e);

        return query;
    }

    public void insert(Object record) {
        insert(dsl(), record);
    }

    public void insert(DSLContext dslContext, Object record) {
        dslContext.insertInto(table)
                .set((record instanceof TableRecord) ? (Record) record : dslContext.newRecord(table, record))
                .execute();
    }

    public Record insertOnGeneratedKey(Object record) {
        return insertOnGeneratedKey(dsl(), record);
    }

    public Record insertOnGeneratedKey(DSLContext dslContext, Object record) {
        return dslContext.insertInto(table)
                .set((record instanceof TableRecord) ? (Record) record : dslContext.newRecord(table, record))
                .returning()
                .fetchOne();
    }

    public void updateByKey(Object record, PK key) {
        updateByKey(dsl(), record, key);
    }

    public void updateByKey(DSLContext dslContext, Object record, PK key) {
        findOneIfNullThrow(dslContext, key);
        update(dslContext, record, getCondition(key));
    }

    public void update(DSLContext dslContext, Object record, Condition condition) {
        dslContext.update(table)
                .set((record instanceof TableRecord) ? (Record) record : dslContext.newRecord(table, record))
                .where(compareCompanyId())
                .and(condition)
                .execute();
    }

    public int delete(PK key) {
        return delete(dsl(), key);
    }

    public int delete(DSLContext dsl, PK key) {
        return delete(dsl, getCondition(key));
    }

    public int delete(Condition condition) {
        return delete(dsl(), condition);
    }

    public int delete(List<Condition> conditions) {
        return delete(dsl(), conditions);
    }

    public int delete(DSLContext dsl, Condition condition) {
        return delete(dsl, Collections.singletonList(condition));
    }

    public int delete(DSLContext dsl, List<Condition> conditions) {
        final DeleteConditionStep<?> query = dsl.deleteFrom((Table<?>) table)
                .where(conditions)
                .and(compareCompanyId());

        return query.execute();
    }

    public int deleteOnIfNullThrow(PK key) {
        findOneIfNullThrow(key);
        return delete(key);
    }

    protected Condition compareCompanyId() {
        return compareCompanyId(getCompanyId());
    }

    protected String getCompanyId() {
        return g != null && g.getUser() != null ? g.getUser().getCompanyId() : null;
    }

    protected Condition compareCompanyId(String companyId) {
        final Field<String> companyIdField = table.field("company_id", String.class);

        if (companyIdField != null && companyId != null)
            return companyIdField.eq(companyId);

        return DSL.trueCondition();
    }

    protected Condition compareCompanyId(TableImpl<?> t) {
        final Field<String> companyIdField = t.field("company_id", String.class);

        if (companyIdField != null && getCompanyId() != null)
            return companyIdField.eq(getCompanyId());

        return DSL.trueCondition();
    }

    public void existsKeyIfNonNullThrow(PK key) {
        existsKeyIfNonNullThrow(key, null);
    }

    public void existsKeyIfNonNullThrow(PK key, String message) {
        final ENTITY entity = findOne(key);
        if (entity != null) {
            getLogger().info("중복된 키가 존재합니다. : {} [{}]", table.getName(), key);
            throw new DuplicateKeyException(message != null ? message + " 중복된 키가 존재합니다." : "중복된 키가 존재합니다.");
        }
    }

    public Integer fetchCount(Condition condition) {
        return dsl().select(DSL.count())
                .from(table)
                .where(compareCompanyId())
                .and(condition)
                .fetchOne(0, int.class);
    }

    public Integer fetchCount(List<Condition> conditions) {
        return dsl().select(DSL.count())
                .from(table)
                .where(conditions)
                .and(compareCompanyId())
                .fetchOne(0, int.class);
    }

    protected SelectHavingStep<Record> fetchCount(SelectJoinStep<Record> joinQuery) {
        return fetchCount(joinQuery, Collections.emptyList());
    }

    protected SelectHavingStep<Record> fetchCount(SelectJoinStep<Record> joinQuery, List<Condition> conditions) {
        final SelectConditionStep<Record> query = query(joinQuery)
                .and(compareCompanyId());

        for (Condition e : conditions)
            query.and(e);

        query.orderBy(DSL.field("null"));

        return query;
    }
}
