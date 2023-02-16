package xyz.jxzou.zblog.mail.template;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import xyz.jxzou.zblog.mail.service.MailService;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Map;

/**
 * MailTemplate
 *
 * @author Jx
 **/
@Component
public class MailTemplate {

    private final JavaMailSender javaMailSender;

    private final MailService mailService;

    @Value("spring.mail.username")
    private String defaultFrom;

    public MailTemplate(JavaMailSender javaMailSender, MailService mailService) {
        this.javaMailSender = javaMailSender;
        this.mailService = mailService;
    }

    @Async("mailTreadPoolTaskExecutor")
    public void simpleMail(String from, String text, String subject, String[] to, String[] cc, String[] bcc, String replyTo, Date sentDate) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(from);
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(text);
        simpleMailMessage.setCc(cc);
        simpleMailMessage.setBcc(bcc);
        simpleMailMessage.setSentDate(sentDate);
        simpleMailMessage.setReplyTo(replyTo);
        javaMailSender.send(simpleMailMessage);
    }

    @Async("mailTreadPoolTaskExecutor")
    public void dbTemplateNameMailSend(String template, String subject, Map<String, Object> params, String... to) throws MessagingException {
        this.send(mimeMessageHelper(true, this.getDefaultFrom(), mailService.dbTemplateNameToText(template, params), subject, to));
    }

    @Async("mailTreadPoolTaskExecutor")
    public void dbTemplateNameMailSend(String template, String from, String subject, Map<String, Object> params, String... to) throws MessagingException {
        this.send(mimeMessageHelper(true, from, mailService.dbTemplateNameToText(template, params), subject, to));
    }

    @Async("mailTreadPoolTaskExecutor")
    public void dbTemplateNameMailSend(String template, Map<String, Object> params, String from, String subject, String[] to, String[] cc, String[] bcc, String replyTo, Date sentDate) throws MessagingException {
        this.send(mimeMessageHelper(true, from, mailService.dbTemplateNameToText(template, params), subject, to, cc, bcc, replyTo, sentDate));
    }

    @Async("mailTreadPoolTaskExecutor")
    public void dbTemplateIdMailSend(String template, Map<String, Object> params, String from, String subject, String... to) throws MessagingException {
        this.send(mimeMessageHelper(true, from, mailService.dbTemplateIdToText(template, params), subject, to));
    }

    @Async("mailTreadPoolTaskExecutor")
    public void dbTemplateIdMailSend(String template, Map<String, Object> params, String subject, String... to) throws MessagingException {
        this.send(mimeMessageHelper(true, this.getDefaultFrom(), mailService.dbTemplateIdToText(template, params), subject, to));
    }

    @Async("mailTreadPoolTaskExecutor")
    public void dbTemplateIdMailSend(String template, Map<String, Object> params, String from, String subject, String[] to, String[] cc, String[] bcc, String replyTo, Date sentDate) throws MessagingException {
        this.send(mimeMessageHelper(true, from, mailService.dbTemplateIdToText(template, params), subject, to, cc, bcc, replyTo, sentDate));
    }

    @Async("mailTreadPoolTaskExecutor")
    public void htmlMail(String template, Map<String, Object> params, String from, String subject, String... to) throws MessagingException {
        this.send(mimeMessageHelper(true, from, mailService.templateToText(template, params), subject, to));
    }

    @Async("mailTreadPoolTaskExecutor")
    public void htmlMail(String template, Map<String, Object> params, String from, String subject, String[] to, String[] cc, String[] bcc, String replyTo, Date sentDate) throws MessagingException {
        this.send(mimeMessageHelper(true, from, mailService.templateToText(template, params), subject, to, cc, bcc, replyTo, sentDate));
    }

    public MimeMessageHelper mimeMessageHelper(boolean isHtml, String text, String from, String subject, String... to) throws MessagingException {
        return this.mimeMessageHelper(isHtml, from, text, subject, to, null, null, null, null);
    }

    public MimeMessageHelper mimeMessageHelper(boolean isHtml, String from, String text, String subject, String[] to, String[] cc, String[] bcc, String replyTo, Date sentDate) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, isHtml);
        helper.setCc(cc);
        helper.setBcc(bcc);
        helper.setReplyTo(replyTo);
        helper.setSentDate(sentDate);
        return helper;
    }

    public String getDefaultFrom() {
        return defaultFrom;
    }

    public void setDefaultFrom(String defaultFrom) {
        this.defaultFrom = defaultFrom;
    }

    public void send(MimeMessageHelper mimeMessageHelper) {
        javaMailSender.send(mimeMessageHelper.getMimeMessage());
    }
}
