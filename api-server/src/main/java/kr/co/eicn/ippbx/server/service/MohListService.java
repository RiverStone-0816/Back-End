package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.exception.DuplicateKeyException;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.MohList;
import kr.co.eicn.ippbx.model.entity.eicn.CompanyServerEntity;
import kr.co.eicn.ippbx.model.enums.ShellCommand;
import kr.co.eicn.ippbx.model.enums.TTSErrorCode;
import kr.co.eicn.ippbx.model.enums.WebSecureActionSubType;
import kr.co.eicn.ippbx.model.enums.WebSecureActionType;
import kr.co.eicn.ippbx.model.form.MohListRequest;
import kr.co.eicn.ippbx.model.form.SoundEditorFormRequest;
import kr.co.eicn.ippbx.server.repository.eicn.MohConfigRepository;
import kr.co.eicn.ippbx.server.repository.eicn.MohListRepository;
import kr.co.eicn.ippbx.server.repository.eicn.WebSecureHistoryRepository;
import kr.co.eicn.ippbx.util.EnumUtils;
import kr.co.eicn.ippbx.util.UrlUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.MohConfig.MOH_CONFIG;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.MohList.MOH_LIST;

@Slf4j
@RequiredArgsConstructor
@Service
public class MohListService extends ApiBaseService {

	private final MohListRepository repository;
	private final MohConfigRepository mohConfigRepository;
	private final WebSecureHistoryRepository webSecureHistoryRepository;
	private final CacheService cacheService;
	private final PBXServerInterface pbxServerInterface;
	private final StorageService fileSystemStorageService;
	private final ProcessService processService;

	@Value("${file.path.asterisk}")
	private String asteriskPath;
	@Value("${file.path.moh}")
	private String savePath;

	public MohList findOneIfNullThrow(String category) {
		return repository.findOneIfNullThrow(category);
	}

	public void updateWebLog(String category, String type) {
		final MohList entity = findOneIfNullThrow(category);
		webSecureHistoryRepository.insert(WebSecureActionType.SOUND, WebSecureActionSubType.of(type), entity.getFileName());
	}

	public String insertOnGeneratedKeyAllPbxServersWithFileStore(MohListRequest form) {
		final MultipartFile soundFile = form.getFile();

		if (soundFile.getSize() >  5 * 1024 * 1024)
			throw new IllegalArgumentException("최대 파일 사이즈는 " + 5 + "MB 까지입니다.");
		if (!StringUtils.endsWithAny(soundFile.getOriginalFilename(), ".wav", ".WAV", ".gsm", ".GSM"))
			throw new IllegalArgumentException("알수없는 파일 확장자입니다.");

		final Integer nextSequence = repository.nextSequence();
		final String categoryCode = String.valueOf(nextSequence);

		final String directoryPath = "/" + g.getUser().getCompanyId() + "/" + categoryCode + "/";

		final List<MohList> checked = repository.findAll(
				DSL.and(MOH_LIST.DIRECTORY.eq(directoryPath).or(MOH_LIST.CATEGORY.eq(categoryCode)).or(MOH_LIST.MOH_NAME.eq(form.getMohName())))
		);

		if (checked.size() > 0)
			throw new DuplicateKeyException("동일 이름의 파일이 존재합니다.");

		final Path path = Paths.get(savePath, directoryPath);
		if (Files.notExists(path)) {
			try {
				Files.createDirectories(path);
			} catch (IOException ignored) {
			}
		}

		this.fileSystemStorageService.store(path, form.getFile());

		final String decodeFileName = UrlUtils.decode(soundFile.getOriginalFilename());

		final MohList record = new MohList();
		record.setCompanySeq(nextSequence);
		record.setCategory(categoryCode);
		record.setMohName(form.getMohName());
		record.setCompanyId(g.getUser().getCompanyId());
		record.setDirectory(directoryPath);
		record.setFileName(decodeFileName);

		repository.insert(record);

		mohConfigRepository.insert(categoryCode, "mode", "files");
		mohConfigRepository.insert(categoryCode, "directory", savePath + directoryPath);

		final List<CompanyServerEntity> companyServerEntities = cacheService.pbxServerList(g.getUser().getCompanyId());

		if (companyServerEntities.size() < 1) {
			try {
				Runtime.getRuntime().exec("/home/ippbxmng/lib/reload_moh.sh");
			} catch (IOException ignore) {
			}
		} else {
			companyServerEntities
						.forEach(e -> {
							DSLContext pbxDsl = pbxServerInterface.using(e.getHost());
							repository.insert(pbxDsl, record);

							mohConfigRepository.insert(pbxDsl, categoryCode, "mode","files");
							mohConfigRepository.insert(pbxDsl, categoryCode, "directory", savePath + directoryPath);

							processService.execute(ShellCommand.SCP_SOUND_MOH, "MOH", e.getHost(), g.getUser().getCompanyId());
						});
		}

		return categoryCode;
	}

	public void deleteWithFileStore(String category) {
		final MohList entity = repository.findOneIfNullThrow(category);

		final Path path = Paths.get(savePath, entity.getDirectory(), entity.getFileName());

		this.fileSystemStorageService.delete(path);

		repository.delete(category);
		mohConfigRepository.delete(MOH_CONFIG.CATEGORY.eq(category));

		cacheService.pbxServerList(g.getUser().getCompanyId())
				.forEach(e -> {
					DSLContext pbxDsl = pbxServerInterface.using(e.getHost());
					repository.delete(pbxDsl, category);
					mohConfigRepository.delete(pbxDsl, MOH_CONFIG.CATEGORY.eq(category));
				});
	}

	public Integer created(SoundEditorFormRequest form) {
		final Integer nextSequence = repository.nextSequence();
		final List<MohList> checked = repository.findAll(
				DSL.and(MOH_LIST.DIRECTORY.eq("/" + nextSequence + "/").or(MOH_LIST.CATEGORY.eq(String.valueOf(nextSequence))).or(MOH_LIST.MOH_NAME.eq(form.getSoundName())))
		);

		if (checked.size() > 0)
			throw new DuplicateKeyException("동일 이름의 파일이 존재합니다.");

		final String filePath = asteriskPath + "/moh/" + g.getUser().getCompanyId();
		final String directory = "/" + nextSequence + "/";

		final Path path = Paths.get(filePath, directory);
		if (Files.notExists(path)) {
			try {
				Files.createDirectories(path);
			} catch (IOException ignored) {
			}
		}

		final Integer ret = TTSService.created(path.toString(), form.getFileName() + ".wav",  form.getComment(), form.getPlaySpeed());
		if (ret < 0) {
			throw new IllegalArgumentException(String.format("음원 만들기 에러 (%s)", message.getEnumText(Objects.requireNonNull(EnumUtils.of(TTSErrorCode.class, ret)))));
		}

		final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.MohList record = new kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.MohList();
		record.setCategory(String.valueOf(nextSequence));
		record.setMohName(form.getSoundName());
		record.setCompanyId(g.getUser().getCompanyId());
		record.setCompanySeq(nextSequence);
		record.setDirectory("/" + g.getUser().getCompanyId() + directory);
		record.setFileName(form.getFileName() + ".wav");

		repository.insert(record);

		final String categoryCode = String.valueOf(nextSequence);

		mohConfigRepository.insert(categoryCode, "mode", "files");
		mohConfigRepository.insert(categoryCode, "directory", path.toString() + File.separator);

		cacheService.pbxServerList(g.getUser().getCompanyId())
				.forEach(e -> {
					DSLContext pbxDsl = pbxServerInterface.using(e.getHost());
					repository.insert(pbxDsl, record);

					mohConfigRepository.insert(pbxDsl, categoryCode, "mode", "files");
					mohConfigRepository.insert(pbxDsl, categoryCode, "directory", path.toString() + File.separator);

					processService.execute(ShellCommand.SCP_SOUND_MOH, "MOH", e.getHost(), g.getUser().getCompanyId());
				});

		return nextSequence;
	}
}
