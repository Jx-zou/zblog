package xyz.jxzou.zblog.service.user.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import xyz.jxzou.zblog.common.core.pojo.ResponseResult;
import xyz.jxzou.zblog.common.exception.enums.CommonResponseEnum;
import xyz.jxzou.zblog.service.user.entity.User;

@RestController
public class UserController {

    @PostMapping("registry")
    public ResponseResult<Void> registry(@RequestBody User user) {
        return CommonResponseEnum.SUCCESS.getResult();
    }
}
