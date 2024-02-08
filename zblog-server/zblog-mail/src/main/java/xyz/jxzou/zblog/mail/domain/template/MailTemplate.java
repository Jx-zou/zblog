package xyz.jxzou.zblog.mail.domain.template;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import xyz.jxzou.zblog.common.core.handler.TemplateFrameworkHandler;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * The type Mail template.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MailTemplate {

    private final JavaMailSender javaMailSender;
    private final TemplateFrameworkHandler templateFrameworkHandler;

    @Value("${spring.mail.username}")
    private String defaultFrom;

    private String getFileName(MultipartFile file) {
        String suffix = "";
        String filename = file.getOriginalFilename();
        if (StringUtils.isBlank(filename)){
            String contentType = file.getContentType();
            if (contentType == null || StringUtils.isBlank(contentType)) {
                return null;
            }
            suffix = contentType.substring(contentType.lastIndexOf("/"), contentType.length() - 1);
            return UUID.randomUUID() + "." + suffix;
        }
        suffix = filename.substring(filename.lastIndexOf("."), filename.length() - 1);
        return UUID.randomUUID() + "." + suffix;
    }

    /**
     * Send string mail.
     *
     * @param from     the from
     * @param text     the text
     * @param subject  the subject
     * @param to       the to
     * @param cc       the cc
     * @param bcc      the bcc
     * @param replyTo  the reply to
     * @param sentDate the sent date
     */
    @Async("mailTreadPoolTaskExecutor")
    public void sendStringMail(String from, String text, String subject, String[] to, String[] cc, String[] bcc, String replyTo, Date sentDate) {
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

    /**
     * Send string mail.
     *
     * @param template the template
     * @param params   the params
     * @param from     the from
     * @param text     the text
     * @param subject  the subject
     * @param sentDate the sent date
     * @param to       the to
     */
    @Async("mailTreadPoolTaskExecutor")
    public void sendStringMail(String template, Map<String, String> params, String from, String text, String subject, Date sentDate, String... to) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(from);
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(templateFrameworkHandler.process(template, params));
        simpleMailMessage.setSentDate(sentDate);
        javaMailSender.send(simpleMailMessage);
    }

    /**
     * Send html mail.
     *
     * @param isHtml        the is html
     * @param from          the from
     * @param text          the text
     * @param subject       the subject
     * @param to            the to
     * @param cc            the cc
     * @param bcc           the bcc
     * @param replyTo       the reply to
     * @param sentDate      the sent date
     * @param multipartFile the multipart file
     * @param inline        the inline
     * @param file          the file
     * @throws MessagingException the messaging exception
     */
    @Async("mailTreadPoolTaskExecutor")
    public void sendHtmlMail(boolean isHtml, String from, String text, String subject, String[] to, String[] cc, String[] bcc, String replyTo, Date sentDate, MultipartFile multipartFile, String inline, File file) throws MessagingException {
        MimeMessageHelper helper = mimeMessageHelper(isHtml, from, text, subject, to);
        helper.setCc(cc);
        helper.setBcc(bcc);
        helper.setReplyTo(replyTo);
        helper.setSentDate(sentDate);
        //添加文件
        if (!multipartFile.isEmpty()) {
            String fileName = getFileName(multipartFile);
            if (StringUtils.isNotBlank(fileName)) {
                helper.addAttachment(fileName, multipartFile);
            }
        }
        //添加附件
        helper.addInline(inline, file);
        javaMailSender.send(helper.getMimeMessage());
    }

    /**
     * Send html mail.
     *
     * @param subject 主题
     * @param text    内容
     * @param to      接收方
     * @throws MessagingException the messaging exception
     */
    @Async("mailTreadPoolTaskExecutor")
    public void sendHtmlMail(String subject, String text, String... to) throws MessagingException {
        javaMailSender.send(mimeMessageHelper(true, defaultFrom, text, subject, to).getMimeMessage());
    }

    /**
     * Send html mail.
     *
     * @param template the template
     * @param subject  the subject
     * @param params   the params
     * @param to       the to
     * @throws MessagingException the messaging exception
     */
    @Async("mailTreadPoolTaskExecutor")
    public void sendHtmlMail(String template, String subject, Map<String, String> params, String... to) throws MessagingException {
        javaMailSender.send(mimeMessageHelper(true, templateFrameworkHandler.process(template, params), null, subject, to).getMimeMessage());
    }

    /**
     * Send attachment html mail.
     *
     * @param from          the from
     * @param subject       the subject
     * @param text          the text
     * @param multipartFile the multipart file
     * @param to            the to
     * @throws MessagingException the messaging exception
     */
    @Async("mailTreadPoolTaskExecutor")
    public void sendAttachmentHtmlMail(String from, String subject, String text, MultipartFile multipartFile, String... to) throws MessagingException {
        MimeMessageHelper helper = mimeMessageHelper(true, from, text, subject, to);
        if (!multipartFile.isEmpty()) {
            String fileName = getFileName(multipartFile);
            if (StringUtils.isNotBlank(fileName)) {
                helper.addAttachment(fileName, multipartFile);
            }
        }
        javaMailSender.send(helper.getMimeMessage());
    }

    /**
     * Send attachment html mail.
     *
     * @param template      the template
     * @param params        the params
     * @param from          the from
     * @param subject       the subject
     * @param multipartFile the multipart file
     * @param to            the to
     * @throws MessagingException the messaging exception
     */
    @Async("mailTreadPoolTaskExecutor")
    public void sendAttachmentHtmlMail(String template,Map<String, String> params, String from, String subject, MultipartFile multipartFile, String... to) throws MessagingException {
        MimeMessageHelper helper = mimeMessageHelper(true, from, templateFrameworkHandler.process(template, params), subject, to);
        if (!multipartFile.isEmpty()) {
            String fileName = getFileName(multipartFile);
            if (StringUtils.isNotBlank(fileName)) {
                helper.addAttachment(fileName, multipartFile);
            }
        }
        javaMailSender.send(helper.getMimeMessage());
    }

    /**
     * Send inline html mail.
     *
     * @param from    the from
     * @param text    the text
     * @param subject the subject
     * @param inline  the inline
     * @param file    the file
     * @param to      the to
     * @throws MessagingException the messaging exception
     */
    @Async("mailTreadPoolTaskExecutor")
    public void sendInlineHtmlMail(String from, String text, String subject,  String inline, File file, String... to) throws MessagingException {
        MimeMessageHelper helper = mimeMessageHelper(true, from, text, subject, to);
        helper.addInline(inline, file);
        javaMailSender.send(helper.getMimeMessage());
    }

    /**
     * Send inline html mail.
     *
     * @param template the template
     * @param params   the params
     * @param from     the from
     * @param text     the text
     * @param subject  the subject
     * @param inline   the inline
     * @param file     the file
     * @param to       the to
     * @throws MessagingException the messaging exception
     */
    @Async("mailTreadPoolTaskExecutor")
    public void sendInlineHtmlMail(String template,Map<String, String> params, String from, String text, String subject,  String inline, File file, String... to) throws MessagingException {
        MimeMessageHelper helper = mimeMessageHelper(true, from, templateFrameworkHandler.process(template, params), subject, to);
        helper.addInline(inline, file);
        javaMailSender.send(helper.getMimeMessage());
    }

    /**
     * Send mime mail.
     *
     * @param mimeMessageHelper the mime message helper
     */
    @Async("mailTreadPoolTaskExecutor")
    public void sendMimeMail(MimeMessageHelper mimeMessageHelper) {
        javaMailSender.send(mimeMessageHelper.getMimeMessage());
    }

    /**
     * Mime message helper mime message helper.
     *
     * @param isHtml  the is html
     * @param text    the text
     * @param from    the from
     * @param subject the subject
     * @param to      the to
     * @return the mime message helper
     * @throws MessagingException the messaging exception
     */
    public MimeMessageHelper mimeMessageHelper(boolean isHtml, String text, String from, String subject, String... to) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom(defaultFrom);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, isHtml);
        return helper;
    }
}
