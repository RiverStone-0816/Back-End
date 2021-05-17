package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.SoundList;
import kr.co.eicn.ippbx.server.model.enums.ShellCommand;
import kr.co.eicn.ippbx.server.model.enums.TTSErrorCode;
import kr.co.eicn.ippbx.server.model.enums.WebSecureActionSubType;
import kr.co.eicn.ippbx.server.model.enums.WebSecureActionType;
import kr.co.eicn.ippbx.server.model.form.SoundEditorFormRequest;
import kr.co.eicn.ippbx.server.model.form.SoundListRequest;
import kr.co.eicn.ippbx.server.model.search.SoundListSearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.SoundListRepository;
import kr.co.eicn.ippbx.server.repository.eicn.WebSecureHistoryRepository;
import kr.co.eicn.ippbx.server.util.EnumUtils;
import kr.co.eicn.ippbx.server.util.UrlUtils;
import kr.co.eicn.ippbx.server.util.page.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

import static kr.co.eicn.ippbx.server.jooq.eicn.tables.SoundList.SOUND_LIST;
import static kr.co.eicn.ippbx.server.util.StringUtils.subStringBytes;

@Slf4j
@RequiredArgsConstructor
@Service
public class SoundListService extends ApiBaseService {

	private final SoundListRepository repository;
	private final WebSecureHistoryRepository webSecureHistoryRepository;
	private final CacheService cacheService;
	private final PBXServerInterface pbxServerInterface;
	private final StorageService fileSystemStorageService;
	private final ProcessService processService;

	@Value("${file.path.ars}")
	private String savePath;
	@Value("${server.schema}")
	private String schema;

	public Pagination<kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.SoundList> pagination(SoundListSearchRequest search) {
		return repository.pagination(search);
	}

	public SoundList findOneIfNullThrow(Integer seq) {
		return repository.findOneIfNullThrow(seq);
	}

	public void updateWebLog(Integer seq, String type) {
		final SoundList entity = findOneIfNullThrow(seq);
		webSecureHistoryRepository.insert(WebSecureActionType.SOUND, WebSecureActionSubType.of(type), entity.getSoundName());
	}

	public Integer insertOnGeneratedKeyAllPbxServersWithFileStore(SoundListRequest form) {
		final MultipartFile soundFile = form.getFile();

		if (soundFile.getSize() >  5 * 1024 * 1024)
			throw new IllegalArgumentException("최대 파일 사이즈는 5MB 까지입니다.");
		if (!StringUtils.endsWithAny(soundFile.getOriginalFilename(), ".wav", ".WAV", ".gsm", ".GSM"))
			throw new IllegalArgumentException("알수없는 파일 확장자입니다.");

		final List<SoundList> duplicatedFileNameSoundList = repository.findAll(SOUND_LIST.SOUND_FILE.eq(soundFile.getOriginalFilename()));
		if (duplicatedFileNameSoundList.size() > 0)
			throw new IllegalArgumentException(soundFile.getOriginalFilename() + "은(는) 이미 존재하는 파일 입니다. 삭제후 다시 업로드해 주세요.");

		final Path directoryPath = Paths.get(savePath, g.getUser().getCompanyId());
		if (Files.notExists(directoryPath)) {
			try {
				Files.createDirectories(directoryPath);
			} catch (IOException ignored) {
			}
		}

		this.fileSystemStorageService.store(directoryPath, soundFile);

		final String decodeFileName = UrlUtils.decode(soundFile.getOriginalFilename());

		final SoundList record = new SoundList();
		final Integer nextSequence = repository.nextSequence();
		record.setSeq(nextSequence);
		record.setSoundName(form.getSoundName());
		record.setSoundFile(decodeFileName);
		record.setComment(subStringBytes(form.getComment(), 990));
		if (form.getProtectArsYn() != null)
			record.setArs(form.getProtectArsYn().getValue());
		record.setCompanyId(g.getUser().getCompanyId());

		repository.insert(record);

		cacheService.pbxServerList(g.getUser().getCompanyId())
				.forEach(e -> {
					try (DSLContext pbxDsl = pbxServerInterface.using(e.getHost())) {
						repository.insert(pbxDsl, record);
						processService.execute(ShellCommand.SCP_SOUND_MOH, "SOUND", e.getHost(), g.getUser().getCompanyId());
					}
				});

		return nextSequence;
	}

	public void deleteWithFileStore(Integer seq) {
		final SoundList entity = repository.findOneIfNullThrow(seq);

		final Path path = Paths.get(savePath, g.getUser().getCompanyId(), entity.getSoundFile());

		this.fileSystemStorageService.delete(path);

		repository.delete(seq);

		cacheService.pbxServerList(g.getUser().getCompanyId())
				.forEach(e -> {
					try (DSLContext pbxDsl = pbxServerInterface.using(e.getHost())) {
						repository.deleteBySoundFile(pbxDsl, entity.getSoundFile());
					}
				});
	}

	public Integer created(SoundEditorFormRequest form) {
		// TTS 파일 생성
		final Integer ret = TTSService.created(Paths.get(savePath, g.getUser().getCompanyId()).toString(), form.getFileName() + ".wav", form.getComment(), form.getPlaySpeed());
		if (ret < 0)
			throw new IllegalArgumentException(String.format("음원 만들기 에러 (%s)", message.getEnumText(Objects.requireNonNull(EnumUtils.of(TTSErrorCode.class, ret)))));

		final SoundList record = new SoundList();
		final Integer nextSequence = repository.nextSequence();
		record.setSeq(nextSequence);
		record.setSoundName(form.getSoundName());
		record.setSoundFile(form.getFileName() + ".wav");
		record.setComment(subStringBytes(form.getComment(), 100));
		record.setCompanyId(g.getUser().getCompanyId());

		repository.insert(record);

		cacheService.pbxServerList(g.getUser().getCompanyId())
				.forEach(e -> {
					try (DSLContext pbxDsl = pbxServerInterface.using(e.getHost())) {
						repository.insert(pbxDsl, record);
						processService.execute(ShellCommand.SCP_SOUND_MOH, "SOUND", e.getHost(), g.getUser().getCompanyId());
					}
				});

		return nextSequence;
	}
}
