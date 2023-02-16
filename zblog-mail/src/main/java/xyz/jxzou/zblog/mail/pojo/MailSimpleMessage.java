package xyz.jxzou.zblog.mail.pojo;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Date;

/**
 * SimpleMessage
 *
 * @author Jx
 **/
public class MailSimpleMessage extends SimpleMailMessage {

    private static final long serialVersionUID = -6264047751548510911L;

    private final JavaMailSender sender;

    public MailSimpleMessage(JavaMailSender sender, String from, String to, String text) {
        super.setFrom(from);
        super.setTo(to);
        super.setText(text);
        this.sender = sender;
    }

    public MailSimpleMessage cc(String... cc) {
        super.setCc(cc);
        return this;
    }

    public MailSimpleMessage bcc(String... bcc) {
        super.setBcc(bcc);
        return this;
    }

    public MailSimpleMessage sentDate(Date sentDate) {
        super.setSentDate(sentDate);
        return this;
    }

    public MailSimpleMessage subject(String subject) {
        super.setSubject(subject);
        return this;
    }

    public void send() {
        sender.send(this);
    }
}
