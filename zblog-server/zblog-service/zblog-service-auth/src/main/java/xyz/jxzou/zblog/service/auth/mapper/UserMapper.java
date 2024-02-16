package xyz.jxzou.zblog.service.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import xyz.jxzou.zblog.service.auth.domain.entity.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("select * from auth.user where account=#{account}")
    User findByAccount(String account);

    @Select("select u.* from auth.user u where u.account=#{account} or u.phone=#{account} or u.mail=#{account}")
    User findByAccountOrPhoneOrMail(String account);

    @Select("select count(0) from auth.user u where u.mail=#{mail}")
    int hasUser(String mail);

    @Select("select u.id from auth.user u where u.mail=#{mail}")
    Long findIdByMail(String mail);
}
