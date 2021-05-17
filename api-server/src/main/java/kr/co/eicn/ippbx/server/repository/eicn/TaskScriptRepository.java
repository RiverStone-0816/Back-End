package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.TaskScript;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.TaskScriptCategory;
import kr.co.eicn.ippbx.server.model.form.TaskScriptFormRequest;
import kr.co.eicn.ippbx.server.model.search.TaskScriptSearchRequest;
import kr.co.eicn.ippbx.server.util.page.Pagination;
import lombok.Getter;
import org.jooq.Condition;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static kr.co.eicn.ippbx.server.jooq.eicn.Tables.TASK_SCRIPT;
import static kr.co.eicn.ippbx.server.jooq.eicn.Tables.TASK_SCRIPT_CATEGORY;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Getter
@Repository
public class TaskScriptRepository extends EicnBaseRepository<TaskScript,kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.TaskScript, Long> {
    protected final Logger logger = LoggerFactory.getLogger(TaskScriptRepository.class);

    private final TaskScriptCategoryRepository categoryRepository;

    TaskScriptRepository(TaskScriptCategoryRepository categoryRepository) {
        super(TASK_SCRIPT,TASK_SCRIPT.ID,kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.TaskScript.class);
        this.categoryRepository = categoryRepository;
    }

    public Long nextId() {
        final TaskScript Id = TASK_SCRIPT.as("ID");
        return dsl.select(DSL.ifnull(DSL.max(Id.ID), 0).add(1)).from(Id).fetchOneInto(Long.class);
    }

    public Pagination<kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.TaskScript> pagination(TaskScriptSearchRequest search) {
        return super.pagination(search, conditions(search), Collections.singletonList(TASK_SCRIPT.CREATED_AT.desc()));
    }

    public void insertOnGeneratedKey(TaskScriptFormRequest form) {
        final TaskScriptCategory scriptCategory = categoryRepository.findOne(TASK_SCRIPT_CATEGORY.ID.eq(form.getCategoryId()));
        final kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.TaskScript scriptRecord = new kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.TaskScript();

        scriptRecord.setId(nextId());
        if (Objects.nonNull(scriptCategory))
            scriptRecord.setCategory(scriptCategory.getId());
        scriptRecord.setTag(form.getTag());
        scriptRecord.setTitle(form.getTitle());
        scriptRecord.setContent(form.getContent());
        scriptRecord.setCompanyId(getCompanyId());


        super.insertOnGeneratedKey(scriptRecord);
    }

    private List<Condition> conditions(TaskScriptSearchRequest search) {
        final List<Condition> conditions = new ArrayList<>();

        if (search.getStartDate() != null)
            conditions.add(TASK_SCRIPT.CREATED_AT.ge(DSL.timestamp(search.getStartDate() + " 00:00:00")));

        if (search.getEndDate() != null)
            conditions.add(TASK_SCRIPT.CREATED_AT.le(DSL.timestamp(search.getEndDate() + " 23:59:59")));

        if (isNotEmpty(search.getTitle()))
            conditions.add(TASK_SCRIPT.TITLE.like("%" + search.getTitle() + "%"));

        if (isNotEmpty(search.getTag()))
            conditions.add(TASK_SCRIPT.TAG.like("%" + search.getTag() + "%"));

        if (search.getCategoryId() != null)
            conditions.add(TASK_SCRIPT.CATEGORY.eq(search.getCategoryId()));


        return conditions;
    }
}