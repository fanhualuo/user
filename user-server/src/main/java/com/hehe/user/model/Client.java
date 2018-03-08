package com.hehe.user.model;

import com.google.common.collect.Maps;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * @author xieqinghe .
 * @date 2017/11/15 上午10:15
 * @email xieqinghe@terminus.io
 */
@Data
public class Client implements ClientDetails, Serializable {
    private static final long serialVersionUID = -5530686158966937529L;

    /**
     * 自增主键
     */
    private Long id;

    /**
     *
     */
    private String clientId;

    /**
     * 应用名
     */
    private String clientName;

    /**
     * 应用户名
     */
    private String clientSecret;

    /**
     * 应用地址
     */
    private String clientUrl;

    /**
     * 应用logo
     */
    private String clientLogoUrl;

    /**
     * 回跳地址
     */
    private Set<String> registeredRedirectUri;
    private String registeredRedirectUri1;

    /**
     * 授权类型
     */
    private Set<String> authorizedGrantTypes;
    private String authorizedGrantTypes1;


    /**
     * 资源id
     */
    private Set<String> resourceIds;
    private String resourceIds1;

    /**
     * 范围
     */
    private Set<String> scope;
    private String scope1;

    /**
     * token失效时间
     */
    private Integer accessTokenValiditySeconds;

    /**
     * 刷新token失效时间
     */
    private Integer refreshTokenValiditySeconds;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 更新时间
     */
    private Date updatedAt;


    @Override
    public boolean isSecretRequired() {
        return true;
    }

    @Override
    public boolean isScoped() {
        return true;
    }


    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_CLIENT"));
    }


    @Override
    public boolean isAutoApprove(String scope) {
        return true;
    }


    @Override
    public Map<String, Object> getAdditionalInformation() {
        Map<String, Object> ad = Maps.newHashMap();
        ad.put("clientName", this.clientName);
        ad.put("clientUrl", this.clientUrl);
        ad.put("clientLogoUrl", this.clientLogoUrl);
        return Collections.unmodifiableMap(ad);
    }

    @Override
    public Set<String> getRegisteredRedirectUri() {
        return registeredRedirectUri1 != null ?
                StringUtils.commaDelimitedListToSet(registeredRedirectUri1) : null;
    }

    @Override
    public Set<String> getAuthorizedGrantTypes() {
        return authorizedGrantTypes1 != null ?
                StringUtils.commaDelimitedListToSet(authorizedGrantTypes1) : null;
    }

    @Override
    public Set<String> getResourceIds() {
        return resourceIds1 != null ?
                StringUtils.commaDelimitedListToSet(resourceIds1) : null;
    }

    @Override
    public Set<String> getScope() {
        return scope1 != null ?
                StringUtils.commaDelimitedListToSet(scope1) : null;
    }
}
