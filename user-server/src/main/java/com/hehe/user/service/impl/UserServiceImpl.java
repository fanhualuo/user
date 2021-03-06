package com.hehe.user.service.impl;

import com.google.common.base.CharMatcher;
import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
import com.hehe.common.model.PageInfo;
import com.hehe.common.model.Paging;
import com.hehe.common.model.Response;
import com.hehe.common.util.Arguments;
import com.hehe.common.util.VerifyUtil;
import com.hehe.user.dao.UserDao;
import com.hehe.user.model.User;
import com.hehe.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @author xieqinghe .
 * @date 2017/11/15 下午4:29
 * @email qinghe101@qq.com
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;


    /**
     * 按照  用户名、邮箱、手机号查询
     * 只要匹配一个，就代表查询成功
     *
     * @param identity
     * @return Response
     */
    @Override
    public Response<User> findByIdentity(String identity) {
        try {

            if (!StringUtils.hasText(identity)) {
                return Response.fail("user.identity.not.null");
            }
            List<User> users = null;
            Map<String, Object> criteria = Maps.newHashMap();
            if (VerifyUtil.verifyMobile(identity)) {
                criteria.put("phone", identity);
                users = userDao.list(criteria);
            } else if (VerifyUtil.verifyEmail(identity) && Arguments.isNullOrEmpty(users)) {
                criteria.clear();
                criteria.put("email", identity);
                users = userDao.list(criteria);
            } else if (Arguments.isNullOrEmpty(users)) {
                criteria.clear();
                criteria.put("username", identity);
                users = userDao.list(criteria);
            }

            if (Arguments.isNullOrEmpty(users)) {
                return Response.ok(null);
            }
            //查找到多个用户，代表该数据库数据存在错误，一个用户有多条记录
            if (users.size() != 1) {
                log.error("存在数据重复的用户：identity:{}", identity);
                return Response.fail("user.find.server.error");
            }
            return Response.ok(users.get(0));
        } catch (Exception e) {
            log.error("failed to find user by identity:{}, cause:{}", identity, Throwables.getStackTraceAsString(e));
            return Response.fail("user.find.fail");
        }
    }

    @Override
    public Response<User> findByPhone(String phone) {
        try {
            if (!StringUtils.hasText(phone)) {
                return Response.fail("user.phone.not.null");
            }
            Map<String, Object> criteria = Maps.newHashMap();
            criteria.put("phone", phone);
            List<User> users = userDao.list(criteria);
            if (Arguments.isNullOrEmpty(users)) {
                return Response.ok(null);
            }
            //查找到多个用户，代表该数据库数据存在错误，一个用户有多条记录
            if (users.size() != 1) {
                log.error("存在数据重复的用户：phone:{}", phone);
                return Response.fail("user.find.server.error");
            }
            return Response.ok(users.get(0));
        } catch (Exception e) {
            log.error("failed to find user by phone:{}, cause:{}", phone, Throwables.getStackTraceAsString(e));
            return Response.fail("user.find.fail");
        }
    }

    @Override
    public Response<User> findByEmail(String email) {
        try {
            if (!StringUtils.hasText(email)) {
                return Response.fail("user.email.not.null");
            }
            if (!CharMatcher.is('@').matchesAnyOf(email)) {
                return Response.fail("user.email.format.error");
            }

            Map<String, Object> criteria = Maps.newHashMap();
            criteria.put("email", email);
            List<User> users = userDao.list(criteria);
            if (Arguments.isNullOrEmpty(users)) {
                return Response.ok(null);
            }
            //查找到多个用户，代表该数据库数据存在错误，一个用户有多条记录
            if (users.size() != 1) {
                log.error("存在数据重复的用户：email:{}", email);
                return Response.fail("user.find.server.error");
            }
            return Response.ok(users.get(0));
        } catch (Exception e) {
            log.error("failed to find user by email:{}, cause:{}", email, Throwables.getStackTraceAsString(e));
            return Response.fail("user.find.fail");
        }
    }

    @Override
    public Response<User> findByUserName(String username) {
        try {
            if (!StringUtils.hasText(username)) {
                return Response.fail("user.username.not.null");
            }
            Map<String, Object> criteria = Maps.newHashMap();
            criteria.put("username", username);
            List<User> users = userDao.list(criteria);
            if (Arguments.isNullOrEmpty(users)) {
                return Response.ok(null);
            }
            //查找到多个用户，代表该数据库数据存在错误，一个用户有多条记录
            if (users.size() != 1) {
                log.error("存在数据重复的用户：username:{}", username);
                return Response.fail("user.find.server.error");
            }
            return Response.ok(users.get(0));
        } catch (Exception e) {
            log.error("failed to find user by username:{}, cause:{}", username, Throwables.getStackTraceAsString(e));
            return Response.fail("user.find.fail");
        }
    }

    @Override
    public Response<User> findById(Long id) {
        try {
            if (id == null) {
                return Response.fail("user.id.not.null");
            }
            return Response.ok(userDao.findById(id));
        } catch (Exception e) {
            log.error("failed to find user by id:{}, cause:{}", id, Throwables.getStackTraceAsString(e));
            return Response.fail("user.find.fail");
        }
    }

    @Override
    public Response<Paging<User>> paging(Integer pageNo, Integer pageSize, Map<String, Object> criteria) {
        try {
            PageInfo pageInfo = new PageInfo(pageNo, pageSize);
            return Response.ok(userDao.paging(pageInfo.getOffset(), pageInfo.getLimit(), criteria));
        } catch (Exception e) {
            log.error("failed to paging user by pageNo:{} pageSize:{}, cause:{}", pageNo, pageSize, Throwables.getStackTraceAsString(e));
            return Response.fail("user.paging.fail");
        }
    }

    @Override
    public Response<List<User>> list(Map<String, Object> criteria) {
        try {
            return Response.ok(userDao.list(criteria));
        } catch (Exception e) {
            log.error("failed to list user, cause:{}", Throwables.getStackTraceAsString(e));
            return Response.fail("user.list.fail");
        }
    }

    @Override
    public Response<Long> create(User user) {
        try {
            //密码
            if (StringUtils.hasText(user.getPassword())) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            user.setType(1);
            user.setEnabled(1);
            user.setLocked(0);
            userDao.create(user);
            return Response.ok(user.getId());
        } catch (Exception e) {
            log.error("failed to create user, cause:{}", Throwables.getStackTraceAsString(e));
            return Response.fail("user.create.fail");
        }
    }

    @Override
    public Response<Boolean> update(User user) {
        try {
            if (user.getId() == null) {
                return Response.fail("user.id.not.null");
            }
            //密码
            if (StringUtils.hasText(user.getPassword())) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            return Response.ok(userDao.update(user));
        } catch (Exception e) {
            log.error("failed to update user, cause:{}", Throwables.getStackTraceAsString(e));
            return Response.fail("user.update.fail");
        }
    }

    @Override
    public Response<Boolean> updatePhone(User user) {
        try {
            if (user.getId() == null) {
                return Response.fail("user.id.not.null");
            }
            return Response.ok(userDao.updatePhone(user));
        } catch (Exception e) {
            log.error("failed to update user, cause:{}", Throwables.getStackTraceAsString(e));
            return Response.fail("user.update.fail");
        }
    }

    @Override
    public Response<Boolean> updateEmail(User user) {
        try {
            if (user.getId() == null) {
                return Response.fail("user.id.not.null");
            }
            return Response.ok(userDao.updateEmail(user));
        } catch (Exception e) {
            log.error("failed to update user, cause:{}", Throwables.getStackTraceAsString(e));
            return Response.fail("user.update.fail");
        }
    }

    @Override
    public Response<Boolean> delete(Long id) {
        try {
            if (id == null) {
                return Response.fail("user.id.not.null");
            }
            return Response.ok(userDao.delete(id));
        } catch (Exception e) {
            log.error("failed to delete user by id:{}, cause:{}", id, Throwables.getStackTraceAsString(e));
            return Response.fail("user.delete.fail");
        }
    }
}
