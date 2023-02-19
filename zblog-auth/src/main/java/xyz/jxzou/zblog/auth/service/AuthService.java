package xyz.jxzou.zblog.auth.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import xyz.jxzou.zblog.auth.domain.vo.JwtUser;
import xyz.jxzou.zblog.auth.domain.vo.RUserVo;
import xyz.jxzou.zblog.common.util.pojo.ResponseResult;
import xyz.jxzou.zblog.common.exception.model.exception.ServletException;

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
     * @throws ServletException the servlet exception
     */
    Map<String, String> createPublicKey(String cid) throws Exception;

    /**
     * Login string.
     *
     * @param authUser 认证封装对象
     * @param cid   cid
     * @return the string
     * @throws Exception the exception
     */
    String login(JwtUser authUser, String cid) throws Exception;


    ResponseResult<Void> registry(RUserVo rUserVo, String cid) throws Exception;
}
