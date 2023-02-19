package xyz.jxzou.zblog.mail.service;

public interface MailService {

    void send(String mail, String cid) throws Exception;
}
