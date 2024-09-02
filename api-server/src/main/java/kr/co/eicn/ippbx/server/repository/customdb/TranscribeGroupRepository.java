package kr.co.eicn.ippbx.server.repository.customdb;

import kr.co.eicn.ippbx.meta.jooq.customdb.tables.CommonTranscribeGroup;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.records.CommonTranscribeGroupRecord;
import kr.co.eicn.ippbx.model.entity.customdb.TranscribeGroupEntity;
import kr.co.eicn.ippbx.model.form.TranscribeFileFormRequest;
import kr.co.eicn.ippbx.model.form.TranscribeGroupFormRequest;
import kr.co.eicn.ippbx.model.search.TranscribeDataSearchRequest;
import kr.co.eicn.ippbx.model.search.TranscribeGroupSearchRequest;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.Getter;
import org.apache.catalina.webresources.FileResource;
import org.apache.commons.lang3.StringUtils;
import org.jooq.Condition;
import org.jooq.Record;
import org.jooq.UpdateSetMoreStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.replaceEach;

@Getter
public class TranscribeGroupRepository extends CustomDBBaseRepository<CommonTranscribeGroup, TranscribeGroupEntity, Integer> {
    protected final Logger logger = LoggerFactory.getLogger(TranscribeGroupRepository.class);

    private final CommonTranscribeGroup TABLE;

    public TranscribeGroupRepository(String companyId) {
        super(new CommonTranscribeGroup(companyId), new CommonTranscribeGroup(companyId).SEQ, TranscribeGroupEntity.class);
        this.TABLE = new CommonTranscribeGroup(companyId);
    }

    public Pagination<TranscribeGroupEntity> pagination(TranscribeGroupSearchRequest search) {
        return super.pagination(search, conditions(search));
    }

    private List<Condition> conditions(TranscribeGroupSearchRequest search) {
        List<Condition> conditions = new ArrayList<>();
        if (search.getTranscribeGroup() != null) {
            conditions.add(TABLE.SEQ.eq(search.getTranscribeGroup()));
        }
        if (StringUtils.isNotEmpty(search.getGroupName())) {
            conditions.add(TABLE.GROUPNAME.contains(search.getGroupName()));
        }
        if (StringUtils.isNotEmpty(search.getUserId())) {
            conditions.add(TABLE.USERID.eq(search.getUserId()));
        }

        return conditions;
    }

    public Record insertOnGeneratedKey(TranscribeGroupFormRequest form) {
        final CommonTranscribeGroupRecord transcribeRecord = new CommonTranscribeGroupRecord(TABLE);

        transcribeRecord.setCompanyId(getCompanyId());
        transcribeRecord.setGroupname(form.getGroupName());
        transcribeRecord.setUserid(form.getUserId());
        transcribeRecord.setStatus("A");
        transcribeRecord.setFilecnt(0);
        transcribeRecord.setRecrate((double) 0);

        final Record record = super.insertOnGeneratedKey(transcribeRecord);

        return record;
    }

    public void update(TranscribeGroupFormRequest form, Integer seq) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final UpdateSetMoreStep<CommonTranscribeGroupRecord> query = dsl.update(TABLE)
                .set(TABLE.GROUPNAME, form.getGroupName())
                .set(TABLE.USERID, form.getUserId());

        query.where(TABLE.SEQ.eq(seq)).execute();
    }

    public void statusUpdate(Integer seq) {
        dsl.update(TABLE)
                .set(TABLE.STATUS, "B")
                .where(TABLE.SEQ.eq(seq))
                .execute();
    }

    public void updateCount(Integer seq, Integer count) {
        dsl.update(TABLE)
                .set(TABLE.FILECNT, +count)
                .where(TABLE.SEQ.eq(seq))
                .execute();
    }

    public int delete(Integer seq) {
        //deleteOnIfNullThrow(seq);
        return delete(dsl(), seq);
    }

    public void uploadFile(TranscribeFileFormRequest form, Integer seq) {
        final Map<String, FileResource> files = new HashMap<>();
        if (Objects.nonNull(form.getFiles())) {
            for (MultipartFile file : form.getFiles()) {
                final Path path = Paths.get(replaceEach("/stt/transcribe/", new String[]{"{0}", "{1}"}, new String[]{g.getUser().getCompanyId(), LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"))}));

                if (file.getSize() > 10 * 1024 * 1024)
                    throw new IllegalArgumentException("최대 파일 사이즈는 10MB 까지입니다.");

                if (Files.notExists(path)) {
                    try {
                        Files.createDirectories(path);
                    } catch (IOException ignored) {
                    }
                }


            }
        }
    }

    public List<TranscribeGroupEntity> list(TranscribeDataSearchRequest search) {
        return findAll(conditions(search));
    }

    private List<Condition> conditions(TranscribeDataSearchRequest search) {
        List<Condition> conditions = new ArrayList<>();

        if (search.getTranscribeGroup() != null)
            conditions.add(TABLE.SEQ.eq(search.getTranscribeGroup()));

        if (StringUtils.isNotEmpty(search.getUserId()))
            conditions.add(TABLE.USERID.eq(search.getUserId()));

        return conditions;
    }
}
