package xyz.jxzou.zblog.upload.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface UploadService {

    Map<String, String> upload(MultipartFile[] files) throws IOException;
}
