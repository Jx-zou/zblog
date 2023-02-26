package xyz.jxzou.zblog.service.business.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xyz.jxzou.zblog.service.business.domain.entity.Template;
import xyz.jxzou.zblog.service.business.domain.vo.TemplateVo;
import xyz.jxzou.zblog.service.business.enums.TemplateType;
import xyz.jxzou.zblog.service.business.mapper.TemplateMapper;
import xyz.jxzou.zblog.service.business.service.TemplateService;

@Slf4j
@Service
@RequiredArgsConstructor
public class TemplateServiceImpl implements TemplateService {

    private final TemplateMapper templateMapper;

    @Override
    public String getTemplate(TemplateType type) {
        return templateMapper.findContentByType(type.getId());
    }

    @Override
    public void save(TemplateVo templateVo) {
        int insert = templateMapper.insert(Template.builder()
                .name(templateVo.getName())
                .type(templateVo.getType())
                .content(templateVo.getContent())
                .build());
        if (insert < 0) {
            log.error("template添加错误");
        }
    }
}
