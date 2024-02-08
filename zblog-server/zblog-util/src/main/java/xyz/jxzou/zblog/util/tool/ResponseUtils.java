package xyz.jxzou.zblog.util.tool;


import com.alibaba.fastjson2.JSON;
import xyz.jxzou.zblog.util.enums.ContentType;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * ResponseUtils
 *
 * @author jx
 */
public class ResponseUtils {

    /**
     * Write json.
     *
     * @param response the response
     * @param obj      the obj
     * @throws IOException the io exception
     */
    public static void writeJson(HttpServletResponse response, Object obj) throws IOException {
        response.setContentType(ContentType.JSON.getCentType());
        response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
        ServletOutputStream outputStream = response.getOutputStream();
        JSON.writeTo(outputStream, obj);
    }

    /**
     * Write image.
     *
     * @param response the response
     * @param image    the image
     * @throws IOException the io exception
     */
    public static void writeImage(HttpServletResponse response, BufferedImage image) throws IOException {
        response.setContentType(ContentType.PNG.getCentType());
        response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
        ServletOutputStream os = response.getOutputStream();
        ImageIO.write(image, ContentType.PNG.getCentType(), os);
        os.close();
    }
}
