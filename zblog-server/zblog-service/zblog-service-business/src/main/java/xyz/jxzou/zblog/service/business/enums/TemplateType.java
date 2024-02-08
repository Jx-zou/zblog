package xyz.jxzou.zblog.service.business.enums;

public enum TemplateType {

    MAIL_CAPTCHA(1, "mailCaptcha", "邮件验证码");

    private final Integer id;
    private final String name;
    private final String desc;

    TemplateType(Integer id, String name, String desc) {
        this.id = id;
        this.name = name;
        this.desc = desc;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public String getDesc() {
        return desc;
    }
}
