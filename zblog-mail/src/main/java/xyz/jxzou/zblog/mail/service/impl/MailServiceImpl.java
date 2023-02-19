package xyz.jxzou.zblog.mail.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import xyz.jxzou.zblog.common.util.tool.Maps;
import xyz.jxzou.zblog.common.util.tool.NanoIDUtils;
import xyz.jxzou.zblog.common.exception.enums.BusinessResponseEnum;
import xyz.jxzou.zblog.common.exception.enums.ServletResponseEnum;
import xyz.jxzou.zblog.common.core.pojo.RSAKeyPair;
import xyz.jxzou.zblog.common.core.pojo.ZBlogContent;
import xyz.jxzou.zblog.common.core.util.RSAUtils;
import xyz.jxzou.zblog.mail.config.MailConfiguration;
import xyz.jxzou.zblog.mail.domain.pojo.MailContent;
import xyz.jxzou.zblog.mail.domain.template.MailTemplate;
import xyz.jxzou.zblog.mail.enums.MailType;
import xyz.jxzou.zblog.mail.service.MailService;
import xyz.jxzou.zblog.service.business.mapper.TemplateMapper;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final RSAKeyPair rsaKeyPair;
    private final MailTemplate mailTemplate;
    private final TemplateMapper templateMapper;
    private final MailConfiguration mailConfiguration;
    @Resource
    private RedisTemplate<String, Object> clientRedisTemplate;
    @Resource
    private RedisTemplate<String, Object> captchaRedisTemplate;

    @Override
    public void send(String mail, String cid) throws Exception {
        //验证cid
        ServletResponseEnum.CLIENT_ID_ERROR.isBlank(cid);
        //解析cid获得clientId并验证
        String clientId = RSAUtils.base64Decrypt(cid, rsaKeyPair.getPrivateKey().getEncoded());
        ServletResponseEnum.CLIENT_ID_ERROR.validateClientId(clientId);
        //检验clientId是否存在
        if (Boolean.FALSE.equals(clientRedisTemplate.hasKey(clientId))) {
            throw ServletResponseEnum.CLIENT_ID_ERROR.newException();
        }
        //检验是否已发送未过期
        String captchaName = ZBlogContent.CAPTCHA_REDIS_PREFIX + clientId;
        if (Boolean.FALSE.equals(captchaRedisTemplate.hasKey(captchaName))) {
            throw BusinessResponseEnum.MAIL_RESEND_ERROR.newException();
        }
        //生成默认6位随机验证码
        String code = NanoIDUtils.alphanumericId(mailConfiguration.getCaptcha().getSize());
        //存入captcha redis库, 并设置过期时间
        captchaRedisTemplate.opsForValue().set(captchaName, code);
        captchaRedisTemplate.expire(captchaName, mailConfiguration.getCaptcha().getExpire(), TimeUnit.SECONDS);

        //使用模板生成内容发送
        Map<String, Object> params = new Maps<String, Object>()
                .put("code", code)
                .hashMap();
        String captchaTemplate = templateMapper.findContentByType(MailType.CAPTCHA.getId());
        mailTemplate.sendHtmlMail(captchaTemplate, MailContent.CAPTCHA_SUBJECT, params, mail);
    }
}
