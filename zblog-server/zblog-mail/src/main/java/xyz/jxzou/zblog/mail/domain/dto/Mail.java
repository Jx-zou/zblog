package xyz.jxzou.zblog.mail.domain.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import java.time.LocalDateTime;

/**
 * The type Mail.
 */
@Data
@Builder
public class Mail {

    /**
     * 邮件ID
     */
    private String id;
    /**
     * 模板ID
     */
    private Long templateId;
    /**
     * 是否Html
     */
    private Boolean isHtml;
    /**
     * 发送方
     */
    @Email
    private String from;
    /**
     * 接收方
     */
    @Email
    private String[] to;
    /**
     * 主题
     */
    private String subject;
    /**
     * 邮件内容
     */
    private String text;
    /**
     * 抄送
     */
    private String[] cc;
    /**
     * 密送
     */
    private String[] bcc;
    /**
     * 回复至
     */
    private String replyTo;
    /**
     * 发送时间
     */
    private LocalDateTime sentDte;
    /**
     * 状态
     */
    private Integer status;

    public Boolean isHtml() {
        return this.isHtml;
    }
}
