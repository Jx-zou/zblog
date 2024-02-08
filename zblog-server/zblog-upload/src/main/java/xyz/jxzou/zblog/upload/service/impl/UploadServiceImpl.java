package xyz.jxzou.zblog.upload.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import xyz.jxzou.zblog.common.core.domain.pojo.CoreContent;
import xyz.jxzou.zblog.common.util.tool.FileUtils;
import xyz.jxzou.zblog.service.business.config.ArticleConfiguration;
import xyz.jxzou.zblog.service.business.domain.entity.Article;
import xyz.jxzou.zblog.service.business.domain.entity.UserArticle;
import xyz.jxzou.zblog.service.business.domain.vo.ArticleVo;
import xyz.jxzou.zblog.service.business.mapper.ArticleMapper;
import xyz.jxzou.zblog.service.business.mapper.UserArticleMapper;
import xyz.jxzou.zblog.upload.domain.vo.FileVo;
import xyz.jxzou.zblog.upload.service.UploadService;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class UploadServiceImpl implements UploadService {

    @Value("${file.path}")
    private String path;

    private final String imageMapping = "images/";

    private final ArticleConfiguration articleConfiguration;

    @Resource
    private RedisTemplate<String, Object> businessRedisTemplate;
    private final ArticleMapper articleMapper;
    private final UserArticleMapper userArticleMapper;

    @Override
    public List<FileVo> uploadPhoto(MultipartFile[] files) throws IOException {
        return this.upload(files);
    }

    @Override
    public void uploadArticle(String title, String desc, String file, String userId) throws IOException {
        long uid = Long.parseLong(userId);
        FileVo fileVo = upload(Collections.singletonList(file.getBytes(StandardCharsets.UTF_8)), "html").get(0);
        log.info(new String(Base64.decodeBase64(title), StandardCharsets.UTF_8));
        log.info(new String(Base64.decodeBase64(desc), StandardCharsets.UTF_8));
        Article article = Article.builder()
                .name(fileVo.getName())
                .title(new String(Base64.decodeBase64(title), StandardCharsets.UTF_8))
                .author(uid)
                .profile(new String(Base64.decodeBase64(desc), StandardCharsets.UTF_8))
                .url(fileVo.getUrl())
                .build();
        articleMapper.insert(article);
        Long articleId = articleMapper.findIdByName(fileVo.getName());
        userArticleMapper.insert(UserArticle.builder().uid(uid).aid(articleId).build());

        int size = articleConfiguration.getPageSize();

        Long count = articleMapper.selectCount(null);
        businessRedisTemplate.opsForValue().set(CoreContent.REDIS_NAME_ARTICLE_TOTAL, count);
        for (int offset = 0; offset < count / size; offset++) {
            List<ArticleVo> articleVos = articleMapper.search(offset, size, null);
            businessRedisTemplate.opsForHash().put(CoreContent.REDIS_NAME_ARTICLE_PAGE, offset + "_" + size, articleVos);
        }
        businessRedisTemplate.expire(CoreContent.REDIS_NAME_ARTICLE_PAGE, 1L, TimeUnit.HOURS);
    }

    public List<FileVo> upload(MultipartFile[] files) throws IOException {
        String[] suffixes = new String[files.length];
        for (int i = 0; i < suffixes.length; i++) {
            String originalFilename = files[i].getOriginalFilename();
            if (StringUtils.isNotBlank(originalFilename)) {
                suffixes[i] = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
            }
        }
        return upload(files, suffixes);
    }

    public List<FileVo> upload(MultipartFile[] files, String... suffixes) throws IOException {
        List<FileVo> fileVos = new ArrayList<>();
        for (int i = 0; i < files.length && i < suffixes.length; i++) {
            String newFilename = FileUtils.nanoFilename(suffixes[i]);
            FileUtils.uploadFile(files[i].getBytes(), path + mapping(suffixes[i]), newFilename);
            fileVos.add(FileVo.builder().name(newFilename).url(mapping(suffixes[i]) + newFilename).build());
        }
        return fileVos;
    }

    public List<FileVo> upload(List<byte[]> files, String... suffixes) throws IOException {
        log.info(Arrays.toString(suffixes));
        List<FileVo> fileVos = new ArrayList<>();
        for (int i = 0; i < files.size() && i < suffixes.length; i++) {
            String newFilename = FileUtils.nanoFilename(suffixes[i]);
            FileUtils.uploadFile(files.get(i), path + mapping(suffixes[i]), newFilename);
            fileVos.add(FileVo.builder().name(newFilename).url(mapping(suffixes[i]) + newFilename).build());
        }
        return fileVos;
    }

    private String mapping(String suffix) {
        for (String s : Arrays.asList("jpg", "jpeg", "png")) {
            if (s.equalsIgnoreCase(suffix)) {
                return imageMapping;
            }
        }

        return "";
    }
}
