package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.TaskScriptXFile;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

import static kr.co.eicn.ippbx.meta.jooq.eicn.Tables.TASK_SCRIPT_X_FILE;

@Getter
@Repository
public class TaskScriptXFileRepository extends EicnBaseRepository<TaskScriptXFile, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.TaskScriptXFile, Long> {
    protected final Logger logger = LoggerFactory.getLogger(TaskScriptXFileRepository.class);

    public TaskScriptXFileRepository() {
        super(TASK_SCRIPT_X_FILE,TASK_SCRIPT_X_FILE.TASK_SCRIPT,kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.TaskScriptXFile.class);
    }

    public List<Long> findAllScriptXFile(Long scriptId) {
        return dsl.select(TASK_SCRIPT_X_FILE.FILE)
                .from(TASK_SCRIPT_X_FILE)
                .where(compareCompanyId())
                .and(TASK_SCRIPT_X_FILE.TASK_SCRIPT.eq(scriptId))
                .fetch( TASK_SCRIPT_X_FILE.FILE);
    }

    public void deleteOneFile(Long fileId) {
        dsl.deleteFrom(TASK_SCRIPT_X_FILE)
                .where(compareCompanyId())
                .and(TASK_SCRIPT_X_FILE.FILE.eq(fileId))
                .execute();
    }
}
