package xyz.jxzou.zblog.mail.enums;

/**
 * MailTemplate
 *
 * @author Jx
 */
public enum MailTemplateInfo {
    /**
     * Captcha mail template.
     */
    CAPTCHA("captcha", "验证码");


    private String name;
    private String subject;

    MailTemplateInfo(String name, String subject) {
        this.name = name;
        this.subject = subject;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets subject.
     *
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Sets subject.
     *
     * @param subject the subject
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }
}
