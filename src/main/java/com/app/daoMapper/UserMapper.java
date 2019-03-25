package com.app.daoMapper;

import com.app.model.UserInfo;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    @Select("select * from user_Info where uid = #{uid}")
    @Results({
            @Result(property = "uid", column = "uid"),
            @Result(property = "userName", column = "user_name"),
            @Result(property = "sex", column = "sex"),
            @Result(property = "age", column = "age")
    })
    UserInfo findByUid(@Param("uid") Integer uid);

    int deleteByPrimaryKey(Integer userId);

    int insert(UserInfo record);

    int insertSelective(UserInfo record);

    UserInfo selectByPrimaryKey(String userId);

    int updateByPrimaryKeySelective(UserInfo record);

    int updateByPrimaryKey(UserInfo record);
}
