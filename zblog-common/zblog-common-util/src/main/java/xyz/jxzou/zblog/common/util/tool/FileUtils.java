package xyz.jxzou.zblog.common.util.tool;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * The type File utils.
 */
@Slf4j
public class FileUtils {

    /**
     * Upload file.
     *
     * @param file     the file
     * @param filepath the filepath
     * @param filename the filename
     * @throws IOException the io exception
     */
    public static void uploadFile(byte[] file, String filepath, String filename) throws IOException {
        File targetFile = new File(filepath);
        if (!targetFile.exists()) {
            boolean mkdirs = targetFile.mkdirs();
            if (mkdirs) {
                log.error("创建文件出现异常");
            }
        }

        BufferedOutputStream bos = new BufferedOutputStream(Files.newOutputStream(Paths.get(filepath + filename)));
        bos.write(file);
        bos.flush();
        bos.close();
    }

    /**
     * Nano filename string.
     *
     * @param suffix the suffix
     * @return the string
     */
    public static String nanoFilename(String suffix) {
        return NanoIdUtils.randomNanoId() + "." + suffix;
    }
}
