package xyz.jxzou.zblog.upload.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import xyz.jxzou.zblog.common.core.annotation.FileCheck;
import xyz.jxzou.zblog.common.core.enums.FileType;
import xyz.jxzou.zblog.common.core.pojo.ResponseResult;
import xyz.jxzou.zblog.upload.service.UploadService;

import java.io.IOException;
import java.util.Map;

@CrossOrigin("")
@RestController
@RequestMapping("/upload")
public class UploadController {

    private final UploadService uploadService;

    @Autowired
    public UploadController(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @PostMapping("/photo")
    @FileCheck(
            supportedSuffixes = {"png", "jpg", "jpeg"},
            type = FileCheck.CheckType.SUFFIX_MAGIC_NUMBER,
            supportedFileTypes = {FileType.PNG, FileType.JPG, FileType.JPEG})
    public ResponseResult<Map<String, String>> uploadPhoto(MultipartFile[] files) throws IOException {
        return new ResponseResult<>(200, uploadService.upload(files));
    }
}
