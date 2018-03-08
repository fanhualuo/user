package com.hehe.user.service;

import com.google.common.base.Throwables;
import com.hehe.common.model.PageInfo;
import com.hehe.common.model.Paging;
import com.hehe.common.model.Response;
import com.hehe.common.util.Arguments;
import com.hehe.user.dao.ClientDao;
import com.hehe.user.model.Client;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 应用信息，缓存到redis或内存当中
 *
 * @author xieqinghe .
 * @date 2017/11/15 上午10:43
 * @email xieqinghe@terminus.io
 */
@Slf4j
@Service
public class ClientServiceImp implements ClientService {


    @Autowired
    private ClientDao clientDao;


    @Override
    public Response<Client> findByClientId(String clientId) {
        try {
            if (Arguments.isNull(clientId)) {
                return Response.fail("client.id.not.null");
            }
            return Response.ok(clientDao.findByClientId(clientId));
        } catch (Exception e) {
            log.error("find client error, client:{}, error:{} .", clientId, Throwables.getStackTraceAsString(e));
            return Response.fail("client.find.fail");
        }
    }

    @Override
    public Response<Client> findById(Long id) {
        try {
            return Response.ok(clientDao.findById(id));
        } catch (Exception e) {
            log.error("find client by id error id:{}, cause:{}", id, Throwables.getStackTraceAsString(e));
            return Response.fail("client.find.fail");
        }
    }

    @Override
    public Response<Paging<Client>> paging(Integer pageNo, Integer pageSize, Map<String, Object> criteria) {
        try {
            PageInfo pageInfo = new PageInfo(pageNo, pageSize);
            return Response.ok(clientDao.paging(pageInfo.getOffset(), pageInfo.getLimit(), criteria));
        } catch (Exception e) {
            log.error("paging client error pageNo:{} pageSize:{}, cause:{}", pageNo, pageSize, Throwables.getStackTraceAsString(e));
            return Response.fail("client.find.paging.fail");
        }
    }

    @Override
    public Response<List<Client>> list(Map<String, Object> criteria) {
        try {
            return Response.ok(clientDao.list(criteria));
        } catch (Exception e) {
            log.error("list client error criteria:{}, cause:{}", criteria, Throwables.getStackTraceAsString(e));
            return Response.fail("client.find.list.fail");
        }
    }

    @Override
    public Response<Long> create(Client client) {
        try {
            clientDao.create(client);
            return Response.ok(client.getId());
        } catch (Exception e) {
            log.error("failed to create  client, cause:{}", Throwables.getStackTraceAsString(e));
            return Response.fail("client.create.fail");
        }
    }

    @Override
    public Response<Boolean> update(Client client) {
        try {
            return Response.ok(clientDao.update(client));
        } catch (Exception e) {
            log.error("failed to update client, cause:{}", Throwables.getStackTraceAsString(e));
            return Response.fail("client.update.fail");
        }
    }

    @Override
    public Response<Boolean> delete(Long id) {
        try {
            return Response.ok(clientDao.delete(id));
        } catch (Exception e) {
            log.error("failed to delete us clients by id:{}, cause:{}", id, Throwables.getStackTraceAsString(e));
            return Response.fail("client.delete.fail");
        }
    }


    private static Client client1 = new Client();
    private static Client client2 = new Client();

    static {
        /**
         *应用名、秘钥凭证、应用id、应用角色、应用授权范围、应用权限、可重定向url、资源、access_token有效期(N)、refresh_token有效期(N)
         */
//        client1.setClientId("client_1");
//        client1.setClientSecret("1234567");
//        client1.setId(1L);
//        client1.setAuthorizedGrantTypes(Sets.newHashSet("authorization_code", "client_credentials", "password", "refresh_token"));
//        client1.setScope(Sets.newHashSet("select", "update", "create"));
//        client1.setRegisteredRedirectUri(Sets.newHashSet("http://www.baidu.com"));
//        client1.setResourceIds(Sets.newHashSet("user.center"));
//        client1.setAccessTokenValiditySeconds(1209600);
//        client1.setRefreshTokenValiditySeconds(31536000);
//
//        client2.setClientId("client_2");
//        client2.setClientSecret("1234567");
//        client2.setId(2L);
//        client2.setAuthorizedGrantTypes(Sets.newHashSet("authorization_code", "client_credentials", "password", "refresh_token"));
//        client2.setScope(Sets.newHashSet("select", "update", "create"));
//        client2.setRegisteredRedirectUri(Sets.newHashSet("http://www.baidu.com"));
//        client2.setResourceIds(Sets.newHashSet("user.center"));
//        client2.setAccessTokenValiditySeconds(1209600);
//        client2.setRefreshTokenValiditySeconds(31536000);
    }


}
