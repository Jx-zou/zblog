package xyz.jxzou.zblog.upload.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import xyz.jxzou.zblog.common.util.tool.FileUtils;
import xyz.jxzou.zblog.service.business.domain.entity.Article;
import xyz.jxzou.zblog.service.business.domain.entity.UserArticle;
import xyz.jxzou.zblog.service.business.mapper.ArticleMapper;
import xyz.jxzou.zblog.service.business.mapper.UserArticleMapper;
import xyz.jxzou.zblog.upload.domain.vo.FileVo;
import xyz.jxzou.zblog.upload.service.UploadService;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UploadServiceImpl implements UploadService {

    @Value("${project.host}")
    private String host;

    private final String uploadPath = "/data/upload/images/";

    private final ArticleMapper articleMapper;
    private final UserArticleMapper userArticleMapper;

    @Override
    public List<FileVo> uploadPhoto(MultipartFile[] files) throws IOException {
        return this.upload(files);
    }

    @Override
    public void uploadArticle(String title, String desc, MultipartFile file, String userId) throws IOException {
        long uid = Long.parseLong(userId);
        FileVo fileVo = upload(new MultipartFile[]{file}).get(0);
        Article article = Article.builder().name(fileVo.getName()).title(title).author(uid).desc(desc).url(fileVo.getUrl()).build();
        articleMapper.insert(article);
        Long articleId = articleMapper.findIdByName(fileVo.getName());
        userArticleMapper.insert(UserArticle.builder().uid(uid).aid(articleId).build());
    }

    public List<FileVo> upload(MultipartFile[] files) throws IOException {
        List<FileVo> fileVos = new ArrayList<>();
        for (MultipartFile file : files) {
            String originalFilename = file.getOriginalFilename();
            assert originalFilename != null;
            String suffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
            String newFilename = FileUtils.nanoFilename(suffix);
            FileUtils.uploadFile(file.getBytes(), uploadPath, newFilename);
            FileVo.builder().name(newFilename).url("https://" + host + uploadPath + newFilename);
        }
        return fileVos;
    }
}
