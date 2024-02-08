package xyz.jxzou.zblog.mail.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import xyz.jxzou.zblog.common.core.config.CaptchaProp;
import xyz.jxzou.zblog.common.core.domain.pojo.CoreContent;
import xyz.jxzou.zblog.common.core.domain.pojo.SafetyManager;
import xyz.jxzou.zblog.common.core.util.RSAUtils;
import xyz.jxzou.zblog.common.exception.enums.BusinessResponseEnum;
import xyz.jxzou.zblog.common.util.tool.Maps;
import xyz.jxzou.zblog.common.util.tool.NanoIDUtils;
import xyz.jxzou.zblog.mail.domain.pojo.MailContent;
import xyz.jxzou.zblog.mail.domain.template.MailTemplate;
import xyz.jxzou.zblog.mail.enums.MailType;
import xyz.jxzou.zblog.mail.service.MailService;
import xyz.jxzou.zblog.service.business.mapper.TemplateMapper;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final CaptchaProp captchaConfig;
    private final MailTemplate mailTemplate;
    private final TemplateMapper templateMapper;
    private final SafetyManager safetyManager;
    @Resource
    private RedisTemplate<String, Object> captchaRedisTemplate;

    @Override
    public void send(String mail, String clientId) throws Exception {
        //解密mail并校验
        String plaintMail = RSAUtils.base64Decrypt(mail, safetyManager.getRsaPrivetKey());
        BusinessResponseEnum.MAIL_BOX_ERROR.validateMail(plaintMail);

        //检验是否已发送未过期
        String captchaName = CoreContent.REDIS_PREFIX_CAPTCHA + clientId;
        if (Boolean.TRUE.equals(captchaRedisTemplate.hasKey(captchaName))) {
            Long expire = captchaRedisTemplate.getExpire(captchaName);
            BusinessResponseEnum.REDIS_EXPIRE_ERROR.isNull(expire);
            if (expire < 4 * 60) {
                throw BusinessResponseEnum.MAIL_RESEND_ERROR.newException();
            }
        }
        //生成默认6位随机验证码
        CaptchaProp.Param mailParam = captchaConfig.getParam("mail");
        String code = NanoIDUtils.numericId(mailParam.getSize());

        //使用模板生成内容发送
        Map<String, String> params = new Maps<String, String>().put("code", code).hashMap();
        String captchaTemplate = templateMapper.findContentByType(MailType.CAPTCHA.getId());
        if (StringUtils.isBlank(captchaTemplate)) {
            log.error("未找到模板");
            return;
        }
        mailTemplate.sendHtmlMail(captchaTemplate, MailContent.CAPTCHA_SUBJECT, params, plaintMail);

        //存入captcha redis库, 并设置过期时间
        captchaRedisTemplate.opsForValue().set(captchaName, code);
        captchaRedisTemplate.expire(captchaName, mailParam.getExpire(), TimeUnit.SECONDS);
    }
}
