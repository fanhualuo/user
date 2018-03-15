package com.hehe.user.dao;

import com.google.common.collect.ImmutableMap;
import com.hehe.common.dao.MyBatisDao;
import com.hehe.user.model.Client;
import org.springframework.stereotype.Repository;

/**
 * @author xieqinghe .
 * @date 2018/1/15 上午11:54
 * @email qinghe101@qq.com
 */
@Repository
public class ClientDao extends MyBatisDao<Client>{

    /**
     * 按照clientI查找
     * @param  clientId
     * @return  Client
     */
    public Client findByClientId(String clientId){
        return getSqlSession().selectOne(sqlId("list"), ImmutableMap.of("clientId",clientId));
    }

}
