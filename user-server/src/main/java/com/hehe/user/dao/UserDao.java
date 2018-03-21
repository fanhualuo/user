package com.hehe.user.dao;

import com.hehe.common.dao.MyBatisDao;
import com.hehe.user.model.User;
import org.springframework.stereotype.Repository;

/**
 * @author xieqinghe .
 * @date 2017/11/14 下午4:49
 * @email qinghe101@qq.com
 */
@Repository
public class UserDao extends MyBatisDao<User> {


    /**
     * 更新手机号
     *
     * @param user
     * @return Boolean
     */
    public Boolean updatePhone(User user) {

        return Boolean.valueOf(getSqlSession().update(sqlId("updatePhone"), user) == 1);
    }

    /**
     * 更新邮箱
     *
     * @param user
     * @return Boolean
     */
    public Boolean updateEmail(User user) {
        return Boolean.valueOf(getSqlSession().update(sqlId("updateEmail"), user) == 1);
    }
}
