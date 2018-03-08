package com.hehe.user.service;

import com.hehe.common.model.Response;
import com.hehe.user.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author xieqinghe .
 * @date 2017/11/14 下午4:35
 * @email xieqinghe@terminus.io
 */
public class MyUserDetailsService implements UserDetailsService {

    private final UserService userService;

    public MyUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    /**
     * 根据用户名获取登录用户信息
     *
     * @param username
     * @return UserDetails
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

//        User user=new User()
//        user.setId(1L)
//        user.setUsername("hehe")
//        user.setEmail("qinghe101@qq.com")
//        user.setPhone("15854026443")
//        user.setPassword("8fc4@cf1636f1278457e3dfae")

        Response<User> response = userService.findByIdentity(username);

        if (!response.isSuccess() || response == null) {
            throw new UsernameNotFoundException("CustomUserDetailsServiceImpl.notFound" + new Object[]{username} + "Username {0} not found");
        } else {
            User user = response.getResult();
            //配置用户角色
            List<GrantedAuthority> authorities = new ArrayList();
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            //是否锁定
            boolean isLocked = false;
            if (!isLocked) {

            }
            return new org.springframework.security.core.userdetails.User(user.getId().toString(),
                    user.getPassword(), true, true,
                    true, !isLocked, Collections.unmodifiableList(authorities));
        }

    }
}
