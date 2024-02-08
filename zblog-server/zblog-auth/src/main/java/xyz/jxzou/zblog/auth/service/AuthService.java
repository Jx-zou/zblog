package xyz.jxzou.zblog.auth.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.multipart.MultipartFile;
import xyz.jxzou.zblog.auth.domain.pojo.vo.LUserVo;
import xyz.jxzou.zblog.auth.domain.pojo.vo.RUserVo;
import xyz.jxzou.zblog.common.exception.model.exception.ServletException;
import xyz.jxzou.zblog.common.util.pojo.ResponseResult;
import xyz.jxzou.zblog.auth.domain.pojo.vo.UserinfoVo;
import xyz.jxzou.zblog.upload.domain.vo.FileVo;

import java.io.IOException;
import java.util.Map;

/**
 * The type Auth service.
 */
public interface AuthService extends UserDetailsService {

    UserDetails loadUserByUsername(String account) throws UsernameNotFoundException;

    /**
     * Create public key string.
     *
     * @param cid the cid
     * @return the string
     * @throws Exception the exception
     */
    Map<String, String> createPublicKey(String cid) throws Exception;

    /**
     * Login string.
     *
     * @param authUser 认证封装对象
     * @param cid      cid
     * @return the string
     * @throws Exception the exception
     */
    Map<String, String> login(LUserVo authUser, String cid) throws Exception;


    /**
     * Registry response result.
     *
     * @param rUserVo the r user vo
     * @param cid     the cid
     * @return the response result
     * @throws Exception the exception
     */
    ResponseResult<Void> registry(RUserVo rUserVo, String cid) throws Exception;

    /**
     * Change userinfo.
     *
     * @param userinfoVo the userinfo vo
     * @param userId     the user id
     */
    void changeUserinfo(UserinfoVo userinfoVo, String userId);

    /**
     * Upload avatar file vo.
     *
     * @param file the file
     * @return the file vo
     * @throws IOException the io exception
     */
    FileVo uploadAvatar(MultipartFile file, String userId) throws IOException;

    void logout(String userId) throws ServletException;
}
