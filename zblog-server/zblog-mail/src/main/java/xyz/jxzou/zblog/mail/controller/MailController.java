package xyz.jxzou.zblog.mail.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.jxzou.zblog.common.core.domain.pojo.CoreContent;
import xyz.jxzou.zblog.common.exception.enums.CommonResponseEnum;
import xyz.jxzou.zblog.common.util.pojo.ResponseResult;
import xyz.jxzou.zblog.mail.service.MailService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;

@Slf4j
@RestController
@RequestMapping("mail")
@RequiredArgsConstructor
@Validated
public class MailController {

    private final MailService mailService;

    @PostMapping("captcha")
    public ResponseResult<Void> sendCaptcha(@RequestBody @NotBlank String mail, HttpServletRequest request) throws Exception {
        mailService.send(mail, (String) request.getAttribute(CoreContent.HTTP_HEADER_CLIENT));
        return CommonResponseEnum.SUCCESS.getResult();
    }
}
