package xyz.jxzou.zblog.service.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import xyz.jxzou.zblog.service.user.entity.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("select * from auth.user where account=#{account}")
    User findByAccount(String account);

    @Select("select * from auth.user u where u.account=#{account} or u.phone=#{account} or u.mail=#{account}")
    User findByAccountOrPhoneOrMail(String account);


}
