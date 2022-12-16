package kr.co.eicn.ippbx.front.controller.api;

import kr.co.eicn.ippbx.front.controller.BaseController;
import io.swagger.annotations.ApiModelProperty;
import kr.co.eicn.ippbx.front.service.api.ChattingApiInterface;
import kr.co.eicn.ippbx.util.ResultFailException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


@RestController
@RequestMapping(value = "api/file", produces = MediaType.APPLICATION_JSON_VALUE)
public class FileApiController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(FileApiController.class);

    @Value("${eicn.file.location}")
    private String fileLocation;
    @Value("${eicn.apiserver}")
    private String apiServer;
    @Value("${eicn.messenger.socket.id}")
    private String messengerSocketId;

    private ChattingApiInterface apiInterface = new ChattingApiInterface();

    public String save(String fileName, byte[] bytes) throws IOException {
        if(fileName.indexOf(".jsp") > 0)
            throw new IllegalArgumentException("파일명을 확인하세요.");

        final String filePath = System.currentTimeMillis() + "_" + System.nanoTime() + "_" + fileName;

        val dir = new File(fileLocation);
        if (!dir.exists())
            dir.mkdirs();

        final File file = new File(dir, filePath);
        try (final FileOutputStream writer = new FileOutputStream(file)) {
            writer.write(bytes);
            writer.flush();

            return filePath;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            boolean deleted = file.delete();
            if (!deleted) logger.error("Failed: delete file: " + file.getAbsolutePath());

            throw e;
        }
    }

    public String saveExcel(String fileName, byte[] bytes) throws IOException {
        if(fileName.indexOf(".xls") < 0)
            throw new IllegalArgumentException("파일명을 확인하세요.");

        final String filePath = System.currentTimeMillis() + "_" + System.nanoTime() + "_" + fileName;

        val dir = new File(fileLocation);
        if (!dir.exists())
            dir.mkdirs();

        final File file = new File(dir, filePath);
        try (final FileOutputStream writer = new FileOutputStream(file)) {
            writer.write(bytes);
            writer.flush();

            return filePath;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            boolean deleted = file.delete();
            if (!deleted) logger.error("Failed: delete file: " + file.getAbsolutePath());

            throw e;
        }
    }

    @PostMapping("")
    public FileMeta post(@RequestParam MultipartFile file) throws IOException, ResultFailException {
        final String filePath = save(file.getOriginalFilename(), file.getBytes());
        return new FileMeta(fileLocation + "/" + filePath, filePath, file.getOriginalFilename());
    }

    @PostMapping("excel")
    public FileMeta postExcel(@RequestParam MultipartFile file) throws IOException, ResultFailException {
        final String filePath = saveExcel(file.getOriginalFilename(), file.getBytes());
        return new FileMeta(fileLocation + "/" + filePath, filePath, file.getOriginalFilename());
    }

    @PostMapping("chatting")
    public FileMeta postChatting(@RequestParam MultipartFile file, @RequestParam String roomId) {
        final ChattingApiInterface.FileUploadForm form = new ChattingApiInterface.FileUploadForm();
        form.setMy_userid(g.getUser().getId());
        form.setMy_username(g.getUser().getIdName());
        form.setCompany_id(g.getUser().getCompanyId());
        form.setBasic_url(g.getSocketList().get(messengerSocketId));
        form.setWeb_url(apiServer);
        form.setRoom_id(roomId);
        form.setOriginalName(file.getOriginalFilename());
        form.setFilePath(file.getName());
        apiInterface.uploadFile(form, file);
        return new FileMeta(fileLocation, fileLocation, file.getOriginalFilename());
    }

    @AllArgsConstructor
    @Data
    public class FileMeta {
        @ApiModelProperty(value = "파일경로", required = true)
        private String filePath;
        @ApiModelProperty(value = "저장 파일명", required = true)
        private String fileName;
        @ApiModelProperty(value = "원 파일명", required = true)
        private String originalName;
    }
}
