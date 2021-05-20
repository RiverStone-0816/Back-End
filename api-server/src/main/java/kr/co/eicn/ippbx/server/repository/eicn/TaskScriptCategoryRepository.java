package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.TaskScriptCategory;
import kr.co.eicn.ippbx.model.form.TaskScriptCategoryFormRequest;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import static kr.co.eicn.ippbx.meta.jooq.eicn.Tables.*;

@Getter
@Repository
public class TaskScriptCategoryRepository extends EicnBaseRepository<TaskScriptCategory, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.TaskScriptCategory, Long> {
    protected final Logger logger = LoggerFactory.getLogger(TaskScriptCategoryRepository.class);


    TaskScriptCategoryRepository() {
        super(TASK_SCRIPT_CATEGORY,TASK_SCRIPT_CATEGORY.ID,kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.TaskScriptCategory.class);
    }

    public void insert(TaskScriptCategoryFormRequest form) {
        dsl.insertInto(TASK_SCRIPT_CATEGORY)
                .set(TASK_SCRIPT_CATEGORY.NAME, form.getName())
                .set(TASK_SCRIPT_CATEGORY.COMPANY_ID, getCompanyId())
                .execute();
    }

    public void deleteWithTaskScript(Long id) {
        dsl.deleteFrom(TASK_SCRIPT)
                .where(TASK_SCRIPT.CATEGORY.eq(id))
                .execute();

        deleteOnIfNullThrow(id);
    }

    //    public List<TaskScriptCategory> categories() {
//        return dsl.select(TASK_SCRIPT_CATEGORY.fields())
//                .from(TASK_SCRIPT_CATEGORY)
//                .orderBy(TASK_SCRIPT_CATEGORY.ID.asc())
//                .fetchInto(TaskScriptCategory.class);
//    }
//

//    public void moveUpTaskScriptCategory(Long id) {
//        final List<TaskScriptCategory> categories = categories();
//        for (int i = 1; i < categories.size(); i++) {
//            final TaskScriptCategory category = categories.get(i);
//            if (!Objects.equals(category.getId(), id))
//                continue;
//
//            Collections.swap(categories, i - 1, i);
//        }
//        updateUiIndex(categories);
//    }
//
//    public void moveDownTaskScriptCategory(Long id) {
//        final List<TaskScriptCategory> categories = categories();
//        for (int i = 0; i < categories.size() - 1; i++) {
//            final TaskScriptCategory category = categories.get(i);
//            if (!Objects.equals(category.getId(), id))
//                continue;
//
//            Collections.swap(categories, i, i + 1);
//        }
//        updateUiIndex(categories);
//    }
//
//    public void updateUiIndex(List<TaskScriptCategory> categories) {
//        final ArrayList<Query> queries = new ArrayList<>();
//        for (int i = 0; i < categories.size(); i++)
//            queries.add(
//                    create.update(TASK_SCRIPT_CATEGORY)
//                            .set(TASK_SCRIPT_CATEGORY.UI_INDEX, (double) (i + 1))
//                            .where(TASK_SCRIPT_CATEGORY.ID.eq(categories.get(i).getId()))
//            );
//
//        create.batch(queries).execute();
// }
}
