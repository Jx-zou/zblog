package xyz.jxzou.zblog.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.jxzou.zblog.auth.pojo.AuthContent;
import xyz.jxzou.zblog.auth.service.AuthService;
import xyz.jxzou.zblog.common.core.pojo.ResponseResult;
import xyz.jxzou.zblog.common.exception.enums.CommonResponseEnum;
import xyz.jxzou.zblog.common.exception.model.exception.BaseException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The type Auth controller.
 */
@CrossOrigin()
@RestController
public class AuthController {

    private final AuthService authService;

    /**
     * Instantiates a new Auth controller.
     *
     * @param authService the auth service
     */
    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Gets public key.
     *
     * @param request  the request
     * @param response the response
     * @return the public key
     * @throws BaseException the base exception
     */
    @PostMapping("pkey")
    public ResponseResult<Void> getPublicKey(HttpServletRequest request, HttpServletResponse response) throws BaseException {
        System.out.println("访问了...");
        response.setHeader(AuthContent.PUBLICKEY_HEADER_NAME, authService.createPublicKey(request.getHeader(AuthContent.CLIENT_HEADER_NAME)));
        return CommonResponseEnum.GET_SUCCESS.getResult();
    }

    /**
     * Login response result.
     *
     * @return the response result
     */
    @PostMapping("login")
    public ResponseResult<Void> login() {
        return CommonResponseEnum.SUCCESS.getResult();
    }
}
