package xyz.jxzou.zblog.mail.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.StringTemplateResolver;
import xyz.jxzou.zblog.mail.service.MailService;

import java.util.Locale;
import java.util.Map;

/**
 * MailServiceImpl
 *
 * @author Jx
 */
@Service
public class MailServiceImpl implements MailService {

    private final TemplateEngine templateEngine;

    private final StringTemplateResolver stringTemplateResolver = new StringTemplateResolver();

    /**
     * Instantiates a new Mail service.
     *
     * @param templateEngine the template engine
     */
    @Autowired
    public MailServiceImpl(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Override
    public String templateToText(String template, Map<String, Object> params) {
        return templateEngine.process(template, new Context(Locale.getDefault(), params));
    }

    @Override
    public String dbTemplateIdToText(String template, Map<String, Object> params){
        templateEngine.setTemplateResolver(stringTemplateResolver);
        return templateEngine.process(template, new Context(Locale.getDefault(), params));
    }

    @Override
    public String dbTemplateNameToText(String template, Map<String, Object> params){
        templateEngine.setTemplateResolver(stringTemplateResolver);
        return templateEngine.process(template, new Context(Locale.getDefault(), params));
    }
}
