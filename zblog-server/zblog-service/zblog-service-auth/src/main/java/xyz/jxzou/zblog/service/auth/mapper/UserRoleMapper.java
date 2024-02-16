package xyz.jxzou.zblog.service.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import xyz.jxzou.zblog.service.auth.domain.entity.UserRole;

@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {

}
