package kr.co.eicn.ippbx.server.repository.customdb;

import kr.co.eicn.ippbx.server.config.Constants;
import kr.co.eicn.ippbx.server.repository.BaseRepository;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.TableField;
import org.jooq.impl.TableImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class CustomDBBaseRepository<TABLE extends TableImpl<?>, ENTITY, PK> extends BaseRepository<TABLE, ENTITY, PK> {
    protected TableField<? extends Record, PK> primaryField;

    @Autowired
    @Qualifier(Constants.BEAN_DSL_CUSTOMDB)
    protected DSLContext dsl;

    public CustomDBBaseRepository(TABLE table, TableField<? extends Record, PK> primaryField, Class<ENTITY> entityClass) {
        super(table, entityClass);
        this.primaryField = primaryField;
    }

    @Override
    protected DSLContext dsl() {
        return dsl;
    }

    /**
     * 반드시 Override 해야 한다.
     */
    @Override
    protected Condition getCondition(PK key) {
        return compareCompanyId()
                .and(primaryField.eq(key));
    }
}
