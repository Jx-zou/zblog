package xyz.jxzou.zblog.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.jxzou.zblog.auth.domain.vo.JwtUser;
import xyz.jxzou.zblog.auth.domain.vo.RUserVo;
import xyz.jxzou.zblog.auth.service.AuthService;
import xyz.jxzou.zblog.common.util.pojo.ResponseResult;
import xyz.jxzou.zblog.common.exception.enums.CommonResponseEnum;
import xyz.jxzou.zblog.common.exception.enums.ServletResponseEnum;
import xyz.jxzou.zblog.common.core.pojo.ZBlogContent;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Map;

/**
 * The type Auth controller.
 */
@CrossOrigin()
@Validated
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Gets public key.
     *
     * @param response the response
     * @return the public key
     */
    @PostMapping("pkey")
    public ResponseResult<Void> getPublicKey(@RequestHeader @Size(min = 24, max = 24) String cid, HttpServletResponse response) throws Exception {
        Map<String, String> map = authService.createPublicKey(cid);
        response.setHeader(ZBlogContent.PUBLICKEY_HEADER_NAME, map.get("pkey"));
        response.setHeader(ZBlogContent.CLIENT_HEADER_NAME, map.get("cid"));
        return CommonResponseEnum.GET_SUCCESS.getResult();
    }

    /**
     * Login response result.
     *
     * @param jwtUser the jwt user
     * @return the response result
     */
    @PostMapping("login")
    public ResponseResult<Void> login(@RequestBody @Valid JwtUser jwtUser, @RequestHeader @NotBlank String cid, HttpServletResponse response) throws Exception {
        response.setHeader(HttpHeaders.AUTHORIZATION, authService.login(jwtUser, cid));
        return ServletResponseEnum.AUTH_LOGIN_SUCCESS.getResult();
    }

    /**
     * Registry response result.
     *
     * @return the response result
     */
    @PostMapping("registry")
    public ResponseResult<Void> registry(@RequestBody @Valid RUserVo rUserVo, @RequestHeader @NotBlank String cid) throws Exception {
        return authService.registry(rUserVo, cid);
    }
}
