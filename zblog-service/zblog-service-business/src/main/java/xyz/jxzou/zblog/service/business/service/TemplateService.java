package xyz.jxzou.zblog.service.business.service;

import xyz.jxzou.zblog.service.business.enums.TemplateType;

public interface TemplateService {
    String getTemplate(TemplateType type);
}
