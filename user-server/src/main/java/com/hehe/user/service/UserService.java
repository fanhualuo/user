package com.hehe.user.service;

import com.hehe.common.model.Paging;
import com.hehe.common.model.Response;
import com.hehe.user.model.User;

import java.util.List;
import java.util.Map;


/**
 * @author xieqinghe .
 * @date 2017/11/14 下午4:35
 * @email qinghe101@qq.com
 */
public interface UserService {

    Response<User> findByIdentity(String identity);

    Response<User> findByPhone(String phone);

    Response<User> findByEmail(String email);

    Response<User> findByUserName(String username);


    /**
     * 查询
     * @param id
     * @return user
     */
    Response<User> findById(Long id);

    /**
     * 分页
     * @param pageNo
     * @param pageSize
     * @param criteria
     * @return Paging<User>
     */
    Response<Paging<User>> paging(Integer pageNo, Integer pageSize, Map<String, Object> criteria);

    /**
     * 列表
     * @param criteria
     * @return List<User>
     */
    Response<List<User>> list(Map<String, Object> criteria);

    /**
     * 创建
     * @param user
     * @return Boolean
     */
    Response<Long> create(User user);

    /**
     * 更新
     * @param user
     * @return Boolean
     */
    Response<Boolean> update(User user);

    /**
     * 删除
     * @param id
     * @return Boolean
     */
    Response<Boolean> delete(Long id);

}
