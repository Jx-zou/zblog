package xyz.jxzou.zblog.core.handler;

import lombok.extern.slf4j.Slf4j;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.StringTemplateResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
public class TemplateFrameworkHandler {

    private static final GroupTemplate groupTemplate;

    static {
        try {
            StringTemplateResourceLoader stringTemplateResourceLoader = new StringTemplateResourceLoader();
            Configuration configuration = Configuration.defaultConfiguration();
            groupTemplate = new GroupTemplate(stringTemplateResourceLoader, configuration);
        } catch (IOException e) {
            log.error("初始化Beetl失败");
            throw new RuntimeException(e);
        }
    }

    public String process(String templateContent, Map<String, String> params) {
        Template template = groupTemplate.getTemplate(templateContent);
        template.binding(params);
        return template.render();
    }
}
