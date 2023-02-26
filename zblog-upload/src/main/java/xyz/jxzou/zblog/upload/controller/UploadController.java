package xyz.jxzou.zblog.upload.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xyz.jxzou.zblog.common.exception.enums.CommonResponseEnum;
import xyz.jxzou.zblog.common.util.annotation.FileCheck;
import xyz.jxzou.zblog.common.util.enums.FileType;
import xyz.jxzou.zblog.common.util.pojo.ResponseResult;
import xyz.jxzou.zblog.upload.domain.vo.FileVo;
import xyz.jxzou.zblog.upload.service.UploadService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/upload")
public class UploadController {

    private final UploadService uploadService;

    @Autowired
    public UploadController(UploadService uploadService) {
        this.uploadService = uploadService;
    }

//    @PostMapping("photo")
//    @FileCheck(
//            supportedSuffixes = {"png", "jpg", "jpeg"},
//            type = FileCheck.CheckType.SUFFIX_MAGIC_NUMBER,
//            supportedFileTypes = {FileType.PNG, FileType.JPG, FileType.JPEG})
//    public ResponseResult<List<FileVo>> photo(MultipartFile[] files) throws IOException {
//        return CommonResponseEnum.SUCCESS.getResult(uploadService.uploadPhoto(files));
//    }

    @PostMapping("article")
    @FileCheck(
            supportedSuffixes = {},
            type = FileCheck.CheckType.SUFFIX_MAGIC_NUMBER,
            supportedFileTypes = {}
    )
    public ResponseResult<Void> article(@NotBlank String title, String desc, MultipartFile file, HttpServletRequest request) throws IOException {
        uploadService.uploadArticle(title, desc, file, (String) request.getAttribute("userId"));
        return CommonResponseEnum.SUCCESS.getResult();
    }
}
