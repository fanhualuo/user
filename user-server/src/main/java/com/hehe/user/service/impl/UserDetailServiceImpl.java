package com.hehe.user.service.impl;

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableMap;
import com.hehe.common.model.PageInfo;
import com.hehe.common.model.Paging;
import com.hehe.common.model.Response;
import com.hehe.common.util.Arguments;
import com.hehe.user.dao.UserDetailDao;
import com.hehe.user.model.UserDetail;
import com.hehe.user.service.UserDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 用户详细信息
 *
 * @author xieqinghe .
 * @date 2018/3/16 下午3:37
 * @email qinghe101@qq.com
 */
@Slf4j
@Service
public class UserDetailServiceImpl implements UserDetailService {
    @Autowired
    private UserDetailDao userDetailDao;

    @Override
    public Response<UserDetail> findById(Long id) {
        try {
            return Response.ok(userDetailDao.findById(id));
        } catch (Exception e) {
            log.error("failed to find user detail by id:{}, cause:{}", id, Throwables.getStackTraceAsString(e));
            return Response.fail("user.detail.find.fail");
        }
    }

    @Override
    public Response<Paging<UserDetail>> paging(Integer pageNo, Integer pageSize, Map<String, Object> criteria) {
        try {
            PageInfo pageInfo = new PageInfo(pageNo, pageSize);
            return Response.ok(userDetailDao.paging(pageInfo.getOffset(), pageInfo.getLimit(), criteria));
        } catch (Exception e) {
            log.error("failed to paging user detail by pageNo:{} pageSize:{}, cause:{}", pageNo, pageSize, Throwables.getStackTraceAsString(e));
            return Response.fail("user.detail.paging.fail");
        }
    }

    @Override
    public Response<UserDetail> findByUserId(Long userId) {
        try {
            if (userId == null) {
                return Response.fail("user.id.not.null");
            }
            List<UserDetail> list = userDetailDao.list(ImmutableMap.of("userId", userId));
            if (Arguments.isNullOrEmpty(list)) {
                return Response.ok(null);
            }
            if (list.size() > 1) {
                log.error("一个用户存在多个详细信息列表: userId:{}", userId);
                return Response.fail("user.detail.list.fail");
            }
            return Response.ok(list.get(0));
        } catch (Exception e) {
            log.error("failed to list user detail, cause:{}", Throwables.getStackTraceAsString(e));
            return Response.fail("user.detail.list.fail");
        }
    }

    @Override
    public Response<Long> create(UserDetail userDetail) {
        try {
            userDetailDao.create(userDetail);
            return Response.ok(userDetail.getId());
        } catch (Exception e) {
            log.error("failed to create user detail, cause:{}", Throwables.getStackTraceAsString(e));
            return Response.fail("user.detail.create.fail");
        }
    }

    @Override
    public Response<Boolean> update(UserDetail userDetail) {
        try {
            return Response.ok(userDetailDao.update(userDetail));
        } catch (Exception e) {
            log.error("failed to update user detail, cause:{}", Throwables.getStackTraceAsString(e));
            return Response.fail("user.detail.update.fail");
        }
    }

    @Override
    public Response<Boolean> delete(Long id) {
        try {
            return Response.ok(userDetailDao.delete(id));
        } catch (Exception e) {
            log.error("failed to delete user detail by id:{}, cause:{}", id, Throwables.getStackTraceAsString(e));
            return Response.fail("user.detail.delete.fail");
        }
    }
}
