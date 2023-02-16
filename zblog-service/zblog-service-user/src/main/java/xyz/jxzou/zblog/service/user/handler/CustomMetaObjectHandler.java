package xyz.jxzou.zblog.service.user.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import xyz.jxzou.zblog.common.core.util.NanoIDUtils;

import java.time.LocalDateTime;

@Component
public class CustomMetaObjectHandler implements MetaObjectHandler {

    private final String projectName = "Z-Blog";

    @Override
    public void insertFill(MetaObject metaObject) {
        System.out.println("自动注入开始...");
        System.out.println("account: " + NanoIDUtils.alphanumericId(26));
        this.strictInsertFill(metaObject, "account", String.class, projectName + NanoIDUtils.alphanumericId(26));
        LocalDateTime time = LocalDateTime.now();
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, time);
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, time);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
    }
}
