package xyz.jxzou.zblog.auth.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xyz.jxzou.zblog.auth.domain.pojo.vo.LUserVo;
import xyz.jxzou.zblog.auth.domain.pojo.vo.RUserVo;
import xyz.jxzou.zblog.auth.service.AuthService;
import xyz.jxzou.zblog.common.core.domain.pojo.CoreContent;
import xyz.jxzou.zblog.common.exception.enums.CommonResponseEnum;
import xyz.jxzou.zblog.common.exception.enums.ServletResponseEnum;
import xyz.jxzou.zblog.common.exception.model.exception.ServletException;
import xyz.jxzou.zblog.common.util.annotation.FileCheck;
import xyz.jxzou.zblog.common.util.enums.FileType;
import xyz.jxzou.zblog.common.util.pojo.ResponseResult;
import xyz.jxzou.zblog.auth.domain.pojo.vo.UserinfoVo;
import xyz.jxzou.zblog.upload.domain.vo.FileVo;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.util.Map;

/**
 * The type Auth controller.
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Gets public key.
     *
     * @param cid the cid
     * @return the public key
     * @throws Exception the exception
     */
    @PostMapping("pkey")
    public ResponseResult<Map<String, String>> getPublicKey(@RequestHeader @NotBlank String cid) throws Exception {
        return CommonResponseEnum.GET_SUCCESS.getResult(authService.createPublicKey(cid));
    }

    /**
     * Login response result.
     *
     * @param user    the jwt user
     * @param request the request
     * @return the response result
     * @throws Exception the exception
     */
    @PostMapping("login")
    public ResponseResult<Map<String, String>> login(@RequestBody @Valid LUserVo user, HttpServletRequest request) throws Exception {
        return ServletResponseEnum.AUTH_LOGIN_SUCCESS.getResult(authService.login(user, (String) request.getAttribute(CoreContent.HTTP_HEADER_CLIENT)));
    }

    /**
     * Logout response result.
     *
     * @param request the request
     * @return the response result
     * @throws ServletException the servlet exception
     */
    @PostMapping("logout")
    public ResponseResult<Void> logout(HttpServletRequest request) throws ServletException {
        authService.logout((String) request.getAttribute("userId"));
        return CommonResponseEnum.SUCCESS.getResult();
    }

    /**
     * Registry response result.
     *
     * @param user    the user
     * @param request the request
     * @return the response result
     * @throws Exception the exception
     */
    @PostMapping("registry")
    public ResponseResult<Void> registry(@RequestBody @Valid RUserVo user, HttpServletRequest request) throws Exception {
        return authService.registry(user, (String) request.getAttribute(CoreContent.HTTP_HEADER_CLIENT));
    }

    /**
     * Change response result.
     *
     * @param userinfoVo the userinfo vo
     * @param request    the request
     * @return the response result
     * @throws Exception the exception
     */
    @PostMapping("info/change")
    public ResponseResult<Void> change(@RequestBody @Valid UserinfoVo userinfoVo, HttpServletRequest request) throws Exception {
        authService.changeUserinfo(userinfoVo, (String) request.getAttribute("userId"));
        return CommonResponseEnum.SUCCESS.getResult();
    }

    /**
     * Photo response result.
     *
     * @param file    the file
     * @param request the request
     * @return the response result
     * @throws IOException the io exception
     */
    @PostMapping("upload/avatar")
    @FileCheck(
            supportedSuffixes = {"png", "jpg", "jpeg"},
            type = FileCheck.CheckType.SUFFIX_MAGIC_NUMBER,
            supportedFileTypes = {FileType.PNG, FileType.JPG, FileType.JPEG})
    public ResponseResult<FileVo> avatar(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {
        return CommonResponseEnum.SUCCESS.getResult(authService.uploadAvatar(file, (String) request.getAttribute("userId")));
    }
}
