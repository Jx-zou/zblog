package xyz.jxzou.zblog.service.business.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.jxzou.zblog.service.business.enums.TemplateType;
import xyz.jxzou.zblog.service.business.mapper.TemplateMapper;
import xyz.jxzou.zblog.service.business.service.TemplateService;

@Service
@RequiredArgsConstructor
public class TemplateServiceImpl implements TemplateService {

    private final TemplateMapper templateMapper;

    @Override
    public String getTemplate(TemplateType type) {
        return templateMapper.findContentByType(type.getId());
    }
}
