package xyz.jxzou.zblog.upload.aspect;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import xyz.jxzou.zblog.common.util.annotation.FileCheck;
import xyz.jxzou.zblog.common.util.enums.FileType;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * prefix为配置文件中的前缀,
 * name为配置的名字
 * havingValue是与配置的值对比值,当两个值相同返回true,配置类生效
 * 需要在yml中进行配置：前缀+名字，值为true，表示该配置文件生效
 **/
@Aspect
@Slf4j
@Component
@ConditionalOnProperty(prefix = "file-check", name = "enabled", havingValue = "true")
public class FileCheckAspect {

    /**
     * 目标方法：被@FileCheck注解的方法即为目标方法
     * 其中@annotation中的值，需要和target方法中参数名称相同（必须相同，但是名称任意）
     *
     * @param joinPoint  连接点
     * @param fileCheck 文件检查
     */
    @Before("@annotation(fileCheck)")
    public void before(JoinPoint joinPoint, FileCheck fileCheck) throws FileUploadException {
        final String[] suffixes = fileCheck.supportedSuffixes();
        final FileCheck.CheckType type = fileCheck.type();
        final FileType[] fileTypes = fileCheck.supportedFileTypes();
        final String message = fileCheck.message();

        // 支持的文件后缀和文件类型有一个为空则返回
        if (ArrayUtils.isEmpty(suffixes) && ArrayUtils.isEmpty(fileTypes)) {
            return;
        }
        Object[] args = joinPoint.getArgs();
        //文件后缀转成set集合
        Set<String> suffixSet = new HashSet<>(Arrays.asList(suffixes));
        for (FileType fileType : fileTypes) {
            suffixSet.add(fileType.getSuffix());
        }
        //文件类型转成set集合
        Set<FileType> fileTypeSet = new HashSet<>(Arrays.asList(fileTypes));
        for (String suffix : suffixes) {
            fileTypeSet.add(FileType.getBySuffix(suffix));
        }
        //对参数是文件的进行校验
        for (Object arg : args) {
            if (arg instanceof MultipartFile) {
                doCheck((MultipartFile) arg, type, suffixSet, fileTypeSet, message);
            } else if (arg instanceof MultipartFile[]) {
                for (MultipartFile file : (MultipartFile[]) arg) {
                    doCheck(file, type, suffixSet, fileTypeSet, message);
                }
            }
        }
    }

    /**
     * 根据指定的检查类型对文件进行校验
     */
    private void doCheck(MultipartFile file, FileCheck.CheckType type, Set<String> suffixSet, Set<FileType> fileTypeSet, String message) throws FileUploadException {
        if (type == FileCheck.CheckType.SUFFIX) {
            doCheckSuffix(file, suffixSet, message);
        } else if (type == FileCheck.CheckType.MAGIC_NUMBER) {
            doCheckMagicNumber(file, fileTypeSet, message);
        } else {
            doCheckSuffix(file, suffixSet, message);
            doCheckMagicNumber(file, fileTypeSet, message);
        }
    }

    /**
     * 验证文件魔数
     */
    private void doCheckMagicNumber(MultipartFile file, Set<FileType> fileTypeSet, String message) throws FileUploadException {
        String magicNumber = readMagicNumber(file);
        String fileName = file.getOriginalFilename();
        assert fileName != null;
        String fileSuffix = fileName.substring(fileName.lastIndexOf(".") + 1).toUpperCase();
        for (FileType fileType : fileTypeSet) {
            if (magicNumber.startsWith(fileType.getMagicNumber()) && fileType.getSuffix().toUpperCase().equalsIgnoreCase(fileSuffix)) {
                return;
            }
        }
        log.error("文件头格式错误：{}", magicNumber);
        throw new FileUploadException(message);
    }

    /**
     * 验证文件后缀
     */
    private void doCheckSuffix(MultipartFile file, Set<String> suffixSet, String message) throws FileUploadException {
        String fileName = file.getOriginalFilename();
        assert fileName != null;
        String fileSuffix = fileName.substring(fileName.lastIndexOf(".") + 1).toUpperCase();
        for (String suffix : suffixSet) {
            if (suffix.toUpperCase().equalsIgnoreCase(fileSuffix)) {
                return;
            }
        }
        log.error("文件后缀格式错误：{}", message);
        throw new FileUploadException(message);
    }

    /**
     * 读取文件，获取文件头
     */
    private String readMagicNumber(MultipartFile file) throws FileUploadException {
        try (InputStream is = file.getInputStream()) {
            byte[] fileHeader = new byte[4];
            int read = is.read(fileHeader, 0, 4);
            if (read == -1){
                log.error("文件头内容缺失");
            }
            return byteArray2Hex(fileHeader);
        } catch (IOException e) {
            log.error("文件读取错误：{0}", e);
            throw new FileUploadException("读取文件失败!");
        }
    }

    /**
     * 字节数组转十六进制
     */
    private String byteArray2Hex(byte[] data) {
        StringBuilder stringBuilder = new StringBuilder();
        if (ArrayUtils.isEmpty(data)) {
            return null;
        }
        for (byte datum : data) {
            int v = datum & 0xFF;
            String hv = Integer.toHexString(v).toUpperCase();
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

}


