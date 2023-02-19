package xyz.jxzou.zblog.mail.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.jxzou.zblog.common.util.pojo.ResponseResult;
import xyz.jxzou.zblog.common.exception.enums.CommonResponseEnum;
import xyz.jxzou.zblog.common.core.pojo.ZBlogContent;
import xyz.jxzou.zblog.mail.service.MailService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("mail/send")
@RequiredArgsConstructor
@Validated
public class MailSendController {

    private final MailService mailService;

    @PostMapping("send/captcha")
    public ResponseResult<Void> sendCaptcha(String mail, HttpServletRequest request) throws Exception {
        mailService.send(mail, request.getHeader(ZBlogContent.CLIENT_HEADER_NAME));
        return CommonResponseEnum.SUCCESS.getResult();
    }
}
