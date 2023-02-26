package xyz.jxzou.zblog.common.core.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import xyz.jxzou.zblog.common.core.domain.pojo.CommonContent;
import xyz.jxzou.zblog.common.util.tool.NanoIDUtils;

import java.time.LocalDateTime;

@Component
public class GlobalMetaObjectHandler implements MetaObjectHandler {

    @Value("${spring.application.name}")
    private String projectName;

    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "account", String.class, projectName + NanoIDUtils.alphanumericId(26));
        this.strictInsertFill(metaObject, "avatar", String.class, CommonContent.BASIC_AVATAR);
        LocalDateTime time = LocalDateTime.now();
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, time);
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, time);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "avatar", String.class, CommonContent.BASIC_AVATAR);
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
    }
}
