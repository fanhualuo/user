package com.hehe.user.service;

import com.hehe.common.model.Paging;
import com.hehe.common.model.Response;
import com.hehe.user.model.UserDetail;

import java.util.Map;

/**
 * @author xieqinghe .
 * @date 2018/3/16 下午3:37
 * @email qinghe101@qq.com
 */
public interface UserDetailService {
    /**
     * 查询
     *
     * @param id
     * @return userDetail
     */
    Response<UserDetail> findById(Long id);

    /**
     * 分页
     *
     * @param pageNo
     * @param pageSize
     * @param criteria
     * @return Paging<UserDetail>
     */
    Response<Paging<UserDetail>> paging(Integer pageNo, Integer pageSize, Map<String, Object> criteria);

    /**
     * 按照userId查找
     *
     * @param userId
     * @return List<UserDetail>
     */
    Response<UserDetail> findByUserId(Long userId);

    /**
     * 创建
     *
     * @param userDetail
     * @return Boolean
     */
    Response<Long> create(UserDetail userDetail);

    /**
     * 更新
     *
     * @param userDetail
     * @return Boolean
     */
    Response<Boolean> update(UserDetail userDetail);

    /**
     * 删除
     *
     * @param id
     * @return Boolean
     */
    Response<Boolean> delete(Long id);
}
