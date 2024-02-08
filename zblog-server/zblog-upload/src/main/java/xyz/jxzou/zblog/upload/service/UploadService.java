package xyz.jxzou.zblog.upload.service;

import org.springframework.web.multipart.MultipartFile;
import xyz.jxzou.zblog.upload.domain.vo.FileVo;

import java.io.IOException;
import java.util.List;

public interface UploadService {

    List<FileVo> uploadPhoto(MultipartFile[] files) throws IOException;

    void uploadArticle(String title, String desc, String file, String userId) throws IOException;
}
