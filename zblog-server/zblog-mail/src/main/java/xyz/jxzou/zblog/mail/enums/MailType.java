package xyz.jxzou.zblog.mail.enums;

import lombok.Data;

public enum MailType {

    CAPTCHA(1,"captcha", "验证码");


    private Integer id;
    private String name;
    private String desc;

    MailType(Integer id, String name, String desc) {
        this.id = id;
        this.name = name;
        this.desc = desc;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
