package xyz.jxzou.zblog.mail.pojo;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * TextMailMessage
 *
 * @author Jx
 **/
public class MailMimeMessage extends MimeMailMessage {

    private static final long serialVersionUID = 2289944361306968738L;

    private MimeMessage mimeMessage;

    private final JavaMailSender sender;

    public MailMimeMessage(JavaMailSender sender) throws MessagingException {
        super(new MimeMessageHelper(sender.createMimeMessage(), true));

        this.sender = sender;
    }

    public MailMimeMessage from(String from) throws MessagingException {
        super.setFrom(from);
        return MailMimeMessage.this;
    }

    public void send(){
        sender.send(this.mimeMessage);
    }
}
