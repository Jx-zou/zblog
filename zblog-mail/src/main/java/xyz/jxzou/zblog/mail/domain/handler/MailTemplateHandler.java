package xyz.jxzou.zblog.mail.domain.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import java.util.Locale;
import java.util.Map;

/**
 * 模板处理器
 */
@Component
@RequiredArgsConstructor
public class MailTemplateHandler {

    private final TemplateEngine templateEngine;

    /**
     * Template to text string.
     *
     * @param template the template
     * @param params   the params
     * @return the string
     */
    public String templateToString(String template, Map<String, Object> params) {
        return templateEngine.process(template, new Context(Locale.getDefault(), params));
    }

    /**
     * Db template id to text string.
     *
     * @param template the template
     * @param params   the params
     * @return the string
     */
    public String StringTemplateToString(StringTemplateResolver stringTemplateResolver, String template, Map<String, Object> params){
        templateEngine.setTemplateResolver(stringTemplateResolver);
        return templateEngine.process(template, new Context(Locale.getDefault(), params));
    }

    /**
     * Db template name to text string.
     *
     * @param template the template
     * @param params   the params
     * @return the string
     */
    public String templateToString(ITemplateResolver templateResolver, String template, Map<String, Object> params){
        templateEngine.setTemplateResolver(templateResolver);
        return templateEngine.process(template, new Context(Locale.getDefault(), params));
    }
}
