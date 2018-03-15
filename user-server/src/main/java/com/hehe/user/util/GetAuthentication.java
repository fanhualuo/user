package com.hehe.user.util;

import com.hehe.user.model.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author xieqinghe .
 * @date 2018/1/13 下午7:36
 * @email qinghe101@qq.com
 */
public class GetAuthentication {

    /**
     * 得到用户认证Authentication
     */
    public UsernamePasswordAuthenticationToken buildToken(User user) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getId().toString(), "N/A",
                true, true, true, true, // lock ext point
                Collections.unmodifiableList(authorities) // role ext point
        );
        return new UsernamePasswordAuthenticationToken(userDetails, "N/A", userDetails.getAuthorities());
    }

}
