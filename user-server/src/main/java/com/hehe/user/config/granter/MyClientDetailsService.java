package com.hehe.user.config.granter;

import com.hehe.common.util.RespHelper;
import com.hehe.user.model.Client;
import com.hehe.user.service.ClientService;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.NoSuchClientException;

/**
 * @author xieqinghe .
 * @date 2017/11/17 下午4:15
 * @email xieqinghe@terminus.io
 */
public class MyClientDetailsService implements ClientDetailsService {
    private ClientService clientService;

    public MyClientDetailsService(ClientService clientService) {
        this.clientService = clientService;
    }

    /**
     * 根据客户端clientId获取登录用户信息
     *
     * @param clientId
     * @return
     * @throws NoSuchClientException
     */
    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        Client client = RespHelper.or500(clientService.findByClientId(clientId));
        if (client == null) {
            throw new NoSuchClientException("No client with requested id: " + clientId);
        }
        return client;
    }
}
