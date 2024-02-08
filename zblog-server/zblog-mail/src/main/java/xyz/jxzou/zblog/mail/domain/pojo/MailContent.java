package xyz.jxzou.zblog.mail.domain.pojo;

public class MailContent {

    public static String CAPTCHA_SUBJECT = "ZBLOG-CAPTCHA";

    public static String createCaptcha(String code) {
        return "<!DOCTYPE html>" +
                "<html lang=\"en\">" +
                "<head>" +
                "    <meta charset=\"UTF-8\">" +
                "    <title>Test</title>" +
                "</head>" +
                "<body>" +
                "<div style=\"width: 80%; margin: 0 auto\">" +
                "    <h1>Hi!</h1>" +
                "    <p>欢迎使用Z-Blog,请勿泄露验证码</p>" +
                "    <hr/>" +
                "    <div style=\"margin: 0 auto\">" +
                "        <span>验证码：</span><h3>" + code + "</h3>" +
                "    </div>" +
                "    <span style=\"margin-right: 20px; font-size: 12px\"></span>" +
                "</div>" +
                "</body>" +
                "</html>";
    }
}
