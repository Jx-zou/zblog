package xyz.jxzou.zblog.util.tool;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * The type File utils.
 */
@Slf4j
public class FileUtils {

    /**
     * 本地写入文件
     *
     * @param file     文件
     * @param filepath 文件路径
     * @param filename 文件名
     * @throws IOException IO异常
     */
    public static void writeFile(byte[] file, String filepath, String filename) throws IOException {
        File targetFile = new File(filepath);
        if (!targetFile.exists()) {
            if (targetFile.mkdirs()) {
                log.error("Failed to initialize file path.");
            }
        }

        BufferedOutputStream bos = new BufferedOutputStream(Files.newOutputStream(Paths.get(filepath, filename)));
        bos.write(file);
        bos.flush();
        bos.close();
    }

    /**
     * 新文件名
     *
     * @param suffix 文件后缀
     * @return 文件名
     */
    public static String nanoFilename(String suffix) {
        return UUID.randomUUID() + suffix;
    }
}
