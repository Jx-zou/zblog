package xyz.jxzou.zblog.upload.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import xyz.jxzou.zblog.common.core.util.FileUtils;
import xyz.jxzou.zblog.upload.service.UploadService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class UploadServiceImpl implements UploadService {

    String uploadPath = "/data/upload/images/";

    @Override
    public Map<String, String> upload(MultipartFile[] files) throws IOException {
        Map<String, String> params = new HashMap<>();
        for (MultipartFile file : files) {
            String originalFilename = file.getOriginalFilename();
            assert originalFilename != null;
            String suffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
            String newFilename = FileUtils.nanoFilename(suffix);
            FileUtils.uploadFile(file.getBytes(), uploadPath, newFilename);
            params.put("url", "http://localhost:8080" + uploadPath + newFilename);
        }
        return params;
    }
}
