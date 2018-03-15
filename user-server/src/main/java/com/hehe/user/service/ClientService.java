package com.hehe.user.service;

import com.hehe.common.model.Paging;
import com.hehe.common.model.Response;
import com.hehe.user.model.Client;

import java.util.List;
import java.util.Map;

/**
 * 应用service
 *
 * @author xieqinghe .
 * @date 2017/11/15 上午10:17
 * @email qinghe101@qq.com
 */
public interface ClientService {

    /**
     * 按照clientId查找
     * @param clientId
     * @return Response<Client>
     */
    Response<Client> findByClientId(String clientId);

    /**
     * 按照id查找
     * @param id
     * @return Response
     */
    Response<Client> findById(Long id);


    /**
     * 分页查询
     * @param pageNo
     * @param pageSize
     * @param criteria
     * @return Response
     */
    Response<Paging<Client>> paging(Integer pageNo, Integer pageSize, Map<String, Object> criteria);


    /**
     * 列表查询
     * @param criteria
     * @return Response
     */
    Response<List<Client>> list(Map<String, Object> criteria);

    /**
     * 创建
     * @param client
     * @return
     */
    Response<Long> create(Client client);

    /**
     * 按照id修改
     * @param client
     * @return
     */
    Response<Boolean> update(Client client);

    /**
     * 删除
     * @param id
     * @return
     */
    Response<Boolean> delete(Long id);

}
