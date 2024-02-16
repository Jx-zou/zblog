package xyz.jxzou.zblog.auth.domain.pojo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import xyz.jxzou.zblog.common.core.domain.pojo.CoreContent;
import xyz.jxzou.zblog.service.auth.domain.entity.Role;
import xyz.jxzou.zblog.service.auth.domain.entity.User;

import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtUser implements UserDetails {

    private static final long serialVersionUID = -9050653464796394256L;

    /**
     * 用户ID
     */
    private Long id;
    /**
     * 密码
     */
    @NotBlank
    @JsonIgnore
    private String password;
    /**
     * 昵称
     */
    @NotBlank
    private String nickname;
    /**
     * 电话
     */
    private String phone;

    /**
     * 电子邮件
     */
    @NotBlank
    private String mail;

    /**
     * desc
     */
    @NotBlank
    private String desc;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 非锁定账户
     */
    private boolean accountNonLocked;

    /**
     * 账户不过期
     */
    private boolean accountNonExpired;

    /**
     * 凭证不过期
     */
    private boolean credentialsNonExpired;

    /**
     * 启用
     */
    private boolean enabled;

    /**
     * 用户角色列表
     */
    private List<String> roles;

    /**
     * 用户权限列表
     */
    private Collection<? extends GrantedAuthority> authorities;

    public static JwtUser create(User user, List<Role> roles, List<String> urls) {
        List<String> roleNames = roles.stream().map(Role::getName).collect(Collectors.toList());
        List<GrantedAuthority> authorities = urls.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        return new JwtUser(user.getId(), user.getAccount(), user.getSecret(), user.getNickname(), user.getPhone(),
                user.getMail(), user.getProfile(), user.getAvatar(), user.getAccountNonLocked(), user.getAccountNonExpired(),
                user.getCredentialsNonExpired(),user.getEnabled(), roleNames, authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return CoreContent.REDIS_PREFIX_USERthis.id;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
