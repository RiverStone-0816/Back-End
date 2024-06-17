package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.RecordEncKey;
import kr.co.eicn.ippbx.model.RecordFile;
import kr.co.eicn.ippbx.model.entity.customdb.EicnCdrEntity;
import kr.co.eicn.ippbx.model.entity.eicn.CompanyServerEntity;
import kr.co.eicn.ippbx.model.entity.eicn.RecordEncFileEntity;
import kr.co.eicn.ippbx.model.enums.*;
import kr.co.eicn.ippbx.server.repository.eicn.RecordEncFileRepository;
import kr.co.eicn.ippbx.server.repository.eicn.WebSecureHistoryRepository;
import kr.co.eicn.ippbx.util.AESFileEncrypt256;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

import static kr.co.eicn.ippbx.util.StringUtils.subStringBytes;
import static org.apache.commons.io.FilenameUtils.*;
import static org.apache.commons.lang3.StringUtils.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class RecordFileService extends ApiBaseService {

    private final RecordEncFileRepository recordEncFileRepository;
    private final WebSecureHistoryRepository webSecureHistoryRepository;
    private final CacheService cacheService;
    private final PBXServerInterface pbxServerInterface;
    private final ProcessService processService;
    private String targetHost;
    @Value("${file.path.temporary.enc}")
    private String encTemporaryPath;

    public List<RecordFile> fetchAll(List<EicnCdrEntity> entities) {
        final List<RecordFile> recordFiles = new ArrayList<>();
        for (EicnCdrEntity entity : entities) {
            Objects.requireNonNull(entity);
            Objects.requireNonNull(entity.getRecordFile());

            if (!entity.getRecordInfo().startsWith("M") || isEmpty(entity.getRecordFile()))
                continue;


            final String recordFile = entity.getRecordFile();
            final String filePath = getFullPath(recordFile);
            final String fileName = getName(recordFile);
            final String fileBaseName = getBaseName(recordFile);
            final String fileExtension = getExtension(recordFile);

            targetHost = defaultString(entity.getHost(), "localhost");

            final LocalDate toRingDate = entity.getRingDate().toLocalDateTime().toLocalDate();

            if (!toRingDate.isEqual(LocalDate.now())) {
                final Optional<CompanyServerEntity> r = cacheService.getCompanyServerList(g.getUser().getCompanyId()).stream()
                        .filter(e -> ServerType.RECORD_SERVER.getCode().equals(e.getType()))
                        .findAny();

                r.ifPresent(companyServerEntity -> targetHost = companyServerEntity.getServer().getHost());
            }

            log.info("log -> [targetHost={}, filePath={}, fileName={}]", targetHost, filePath, fileName);

            File orgFile = new File(recordFile);
            File zipFile = new File(recordFile.concat(".zip"));
            File encFile = new File(recordFile.concat(".enc"));

            if (!orgFile.exists() && !zipFile.exists() && !encFile.exists())
                processService.execute(ShellCommand.SCP_GET_SINGLE_FILE, targetHost, fileName, filePath, "&");

            recordFiles.add(RecordFile.builder()
                                    .filePath(recordFile).kind(RecordFileKind.A).index(0)
                                    .cdr(entity.getSeq())
                                    .uniqueid(entity.getUniqueid())
                                    .dstUniqueid(entity.getDstUniqueid())
                                    .build());

            if (entity.getRecordInfo().startsWith("M_")) {
                final int partialRecordingCount = NumberUtils.toInt(replace(entity.getRecordInfo(), "M_", EMPTY), 0);

                for (int i = 0; i < partialRecordingCount; i++) {
                    final String partialFileName = fileBaseName.concat("_") + (i + 1) + ".".concat(fileExtension);
                    final String replaceFilePath = filePath + partialFileName;
                    final File partialRecordingFile = new File(replaceFilePath);

                    if (!partialRecordingFile.exists())
                        processService.execute(ShellCommand.SCP_GET_SINGLE_FILE, targetHost, partialFileName, filePath, "&");

                    recordFiles.add(RecordFile.builder()
                                            .filePath(replaceFilePath).kind(RecordFileKind.P).index(i + 1)
                                            .cdr(entity.getSeq())
                                            .uniqueid(entity.getUniqueid())
                                            .dstUniqueid(entity.getDstUniqueid())
                                            .build());
                }
            }

            webSecureHistoryRepository.insert(WebSecureActionType.SEARCH, WebSecureActionSubType.PLAY, subStringBytes(fileName, 400));
        }

        return recordFiles;
    }

    @SneakyThrows
    public Result getActualExistingFile(String filePath, String mode) {
        if (filePath.indexOf("../") > 0) {
            log.error("log -> ERROR[error=파일 정보를 찾을 수 없습니다., file={}]", filePath);
            return Result.builder().code(0).message("파일 정보를 찾을 수 없습니다.").build();
        }

        if (!filePath.contains("/data/" + g.getUser().getCompany().getCompanyId())) {
            log.error("log -> ERROR[error=파일 정보를 찾을 수 없습니다., file={}]", filePath);
            return Result.builder().code(0).message("파일 정보를 찾을 수 없습니다.").build();
        }

        final String fileName = getName(filePath);
        final String fullPath = getFullPath(filePath);

        File orgFile = new File(filePath);
        File zipFile = new File(filePath.concat(".zip"));
        File encFile = new File(filePath.concat(".enc"));

        log.info("orgFile-> " + orgFile + "zipFile -> " + zipFile + "encFile -> " + encFile);
        if (!orgFile.exists() && !zipFile.exists() && !encFile.exists()) {
            log.error("log -> ERROR[error=파일 정보를 찾을 수 없습니다., file={}]", filePath);
            return Result.builder().code(0).message("파일 정보를 찾을 수 없습니다.").build();
        }

        if (mode.equals("PLAY")) {
            if (zipFile.isFile() || encFile.isFile()) {
                RecordEncFileEntity encFileEntity = null;
                final Optional<CompanyServerEntity> optionalPbxServer = cacheService.pbxServerList(g.getUser().getCompanyId()).stream().findFirst();
                final RecordEncFileEntity file = recordEncFileRepository.findOneByRecordFile(filePath);

                if (Objects.nonNull(file))
                    encFileEntity = file;
                else if (optionalPbxServer.isPresent()) {
                    encFileEntity = recordEncFileRepository.findOnePBXByRecordFile(filePath);
                }

                if (encFileEntity == null) {
                    log.error("log -> ERROR[error=녹취파일 정보가 DB에 없음., file={}]", filePath);
                    return Result.builder().code(0).message("녹취파일 정보가 DB에 없음.").build();
                }

                final String temporaryPath = replace(encTemporaryPath, "{0}", String.valueOf(System.currentTimeMillis()));

                Files.createDirectories(Paths.get(temporaryPath));

                if (zipFile.isFile()) {
                    log.info("log -> zip query=[/usr/local/bin/7za e -y -p{} {} -o{}]", encFileEntity.getRecordEncKey().getEncKey(), zipFile.getAbsolutePath(), temporaryPath);
                    final ProcessBuilder builder = new ProcessBuilder("/usr/local/bin/7za", "e", "-y", "-p" + encFileEntity.getRecordEncKey().getEncKey() , zipFile.getAbsolutePath(), "-o" + temporaryPath);

                    String command = "/usr/local/bin/7za" + " e" + " -y" + " -p'" + encFileEntity.getRecordEncKey().getEncKey() + "' "
                            + zipFile.getAbsolutePath() + " -o" + temporaryPath;

                    try {
                        final Process process = builder.start();

                        final int exitCode = process.waitFor();

                        if (exitCode == 0) {
                            zipFile = new File(temporaryPath, fileName);
                            if (!zipFile.isFile()) {
                                log.error("log -> ERROR[error=복호화된 녹취파일 생성 실패, file={}]", filePath);
                                return Result.builder().code(-2).message("복호환된 녹취파일 생성 실패").build();
                            }
                        } else {
                            log.error("log -> ERROR[error=프로세스 실행 실패, exitValue={}, file={}, command={}]", exitCode, zipFile.getAbsolutePath(), command);
                            return Result.builder().code(exitCode).message("프로세스 실행 실패").build();
                        }
                    } catch (IOException e) {
                        log.error("Process.execute ERROR[error={}]", e.getMessage());
                    }
                } else if (encFile.isFile()) {
                    final RecordEncKey recordEncKey = encFileEntity.getRecordEncKey();

                    if (!EncType.AES_256.getCode().equals(encFileEntity.getEncAlg())) {
                        log.error("log -> ERROR[error=암호 알고리즘을 알 수 없음, file={}]", filePath);
                        return Result.builder().code(-3).message("암호 알고리즘을 알 수 없음").build();
                    }

                    final AESFileEncrypt256 decoder = new AESFileEncrypt256(recordEncKey.getEncKey());

                    decoder.decrypt(encFile.getPath(), temporaryPath+"/"+fileName);
                }
                return Result.builder().code(1).path(Paths.get(temporaryPath)).fileName(fileName).build();
            } else
                return Result.builder().code(1).path(Paths.get(fullPath)).fileName(fileName).build();
        } else {
            webSecureHistoryRepository.insert(WebSecureActionType.SEARCH, WebSecureActionSubType.DOWN, subStringBytes(fileName, 400));
            return Result.builder().code(1).path(Paths.get(zipFile.isFile() ? zipFile.getPath().replace(zipFile.getName(), "") : (encFile.isFile() ? encFile.getPath().replace(encFile.getName(), "") : fullPath)))
                    .fileName(zipFile.isFile() ? zipFile.getName() : (encFile.isFile() ? encFile.getName() : fileName)).build();
        }
    }

    public boolean removeFile(String filePath) {
        File file = new File(filePath);

        if (file.exists())
            return file.delete();
        else
            return false;
    }

    @Builder
    @Data
    public static class Result {
        private Integer code;
        private String message;
        private Path path;
        private String fileName;
    }
}
