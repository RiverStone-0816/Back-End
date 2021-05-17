package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.server.config.Constants;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.RecordDown;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.RecordDownInfo;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.records.RecordDownRecord;
import kr.co.eicn.ippbx.server.model.entity.customdb.EicnCdrEntity;
import kr.co.eicn.ippbx.server.model.entity.eicn.CompanyServerEntity;
import kr.co.eicn.ippbx.server.model.enums.*;
import kr.co.eicn.ippbx.server.model.form.RecordDownFormRequest;
import kr.co.eicn.ippbx.server.model.search.RecordDownSearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.RecordDownInfoRepository;
import kr.co.eicn.ippbx.server.repository.eicn.RecordDownRepository;
import kr.co.eicn.ippbx.server.repository.eicn.WebSecureHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.stream.Collectors.joining;
import static kr.co.eicn.ippbx.server.jooq.eicn.tables.RecordDown.RECORD_DOWN;
import static kr.co.eicn.ippbx.server.util.StringUtils.subStringBytes;
import static org.apache.commons.io.FilenameUtils.getBaseName;
import static org.apache.commons.lang3.StringUtils.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class RecordDownService extends ApiBaseService {

	private final RecordDownRepository repository;
	private final EicnCdrService eicnCdrService;
	private final RecordDownRepository recordDownRepository;
	private final RecordDownInfoRepository recordDownInfoRepository;
	private final WebSecureHistoryRepository webSecureHistoryRepository;
	private final StorageService fileSystemStorageService;
	private final CacheService cacheService;
	private final ProcessService processService;
	private String targetHost;

	@Value("${file.path.record-multi-down}")
	private String savePath;

	public List<RecordDown>	list(RecordDownSearchRequest search) {
		return repository.findAll(search);
	}

	public void deleteWithFileStore(final Integer seq) {
		final RecordDown entity = findOneIfNullThrow(seq);

		repository.delete(seq);
		recordDownInfoRepository.deleteByParent(seq);

		final Path directoryPath = Paths.get(replace(savePath, "{0}", g.getUser().getCompanyId()));

		this.fileSystemStorageService.delete(directoryPath.resolve(entity.getDownFolder() + ".tar.gz"));

		final Path sameDirectoryNamePath = directoryPath.resolve(entity.getDownFolder());

		if (Files.exists(sameDirectoryNamePath)) {
			this.fileSystemStorageService.loadAll(sameDirectoryNamePath)
					.filter(e -> !Files.isDirectory(e))
					.forEach(e -> this.fileSystemStorageService.delete(Paths.get(sameDirectoryNamePath.toString(), e.toString())));

			this.fileSystemStorageService.delete(sameDirectoryNamePath);
		}

		webSecureHistoryRepository.insert(WebSecureActionType.RECORD_DOWN, WebSecureActionSubType.DEL, "파일");
	}

	public RecordDown findOneIfNullThrow(Integer seq) {
		return repository.findOneIfNullThrow(seq);
	}

	public void recordInBatchesRegister(RecordDownFormRequest form) {
		final RecordDownRecord recordDownRecord = recordDownRepository.insertOnGeneratedKey(form);

		for (Integer sequence : form.getSequences()) {
			final EicnCdrEntity entity = eicnCdrService.getRepository().findOne(sequence);
			if (entity != null && isNotEmpty(entity.getRecordFile())) {
				if (!entity.getRecordInfo().startsWith("M"))
					continue;

				targetHost = defaultIfEmpty(entity.getHost(), Constants.LOCAL_HOST);

				final LocalDate toRingDate = entity.getRingDate().toLocalDateTime().toLocalDate();

				if (!toRingDate.isEqual(LocalDate.now())) {
					final Optional<CompanyServerEntity> r = cacheService.getCompanyServerList(g.getUser().getCompanyId()).stream()
							.filter(e -> Objects.equals(ServerType.RECORD_SERVER, EnumUtils.getEnum(ServerType.class, e.getType())))
							.findAny();

					r.ifPresent(companyServerEntity -> targetHost = companyServerEntity.getServer().getHost());
				}

				final RecordDownInfo record = new RecordDownInfo();
				record.setParent(recordDownRecord.getSeq());
				record.setHost(targetHost);
				record.setOrgFile(entity.getRecordFile());
				record.setSaveasFile(getBaseName(entity.getRecordFile()));
				record.setStatus(RecordDownStatusKind.REQUEST_COMPLETED.getCode());
				record.setCompanyId(g.getUser().getCompanyId());

				recordDownInfoRepository.insert(record);

			}
		}
		processService.execute(ShellCommand.RECORD_MULTI_DOWNLOAD, String.valueOf(recordDownRecord.getValue(RECORD_DOWN.SEQ)));

		webSecureHistoryRepository.insert(WebSecureActionType.SEARCH, WebSecureActionSubType.DOWN, subStringBytes(form.getSequences().stream().map(String::valueOf).collect(joining(",")), 400));
	}
}
