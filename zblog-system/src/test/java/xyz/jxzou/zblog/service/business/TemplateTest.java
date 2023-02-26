package xyz.jxzou.zblog.service.business;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.jxzou.zblog.service.business.domain.vo.TemplateVo;
import xyz.jxzou.zblog.service.business.enums.TemplateType;
import xyz.jxzou.zblog.service.business.service.TemplateService;

@SpringBootTest
public class TemplateTest {

    @Autowired
    private TemplateService templateService;

    @Test
    public void insertTest() {
        TemplateVo templateVo = new TemplateVo();
        templateVo.setName(TemplateType.MAIL_CAPTCHA.getName());
        templateVo.setType(TemplateType.MAIL_CAPTCHA.getId());
        templateVo.setContent("<!DOCTYPE html>" +
                "<html lang=\"en\">" +
                "<head>" +
                "    <meta charset=\"UTF-8\">" +
                "    <title>{title}</title>" +
                "</head>" +
                "<body>" +
                "<div style=\"width: 80%; margin: 0 auto\">" +
                "    <h1>Hi!</h1>" +
                "    <p>欢迎使用Z-Blog,请勿泄露验证码</p>" +
                "    <hr/>" +
                "    <div style=\"margin: 0 auto\">" +
                "        <span>验证码：</span><h3>{code}</h3>" +
                "    </div>" +
                "    <span style=\"margin-right: 20px; font-size: 12px\"></span>" +
                "</div>" +
                "</body>" +
                "</html>");
        templateService.save(templateVo);
    }
}
