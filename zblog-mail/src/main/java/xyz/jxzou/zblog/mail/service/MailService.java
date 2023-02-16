package xyz.jxzou.zblog.mail.service;

import java.util.Map;

/**
 * MailService
 *
 * @author Jx
 */
public interface MailService {

    /**
     * Template to text string.
     *
     * @param template the template
     * @param params   the params
     * @return the string
     */
    String templateToText(String template, Map<String, Object> params);

    /**
     * Db template id to text string.
     *
     * @param templateId the template id
     * @param params     the params
     * @return the string
     */
    String dbTemplateIdToText(String templateId, Map<String, Object> params);

    /**
     * Db template name to text string.
     *
     * @param templateId the template id
     * @param params     the params
     * @return the string
     */
    String dbTemplateNameToText(String templateId, Map<String, Object> params);
}
