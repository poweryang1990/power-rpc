package rpc.sample.api.service;

import power.rpc.client.annotation.RpcClient;
import rpc.sample.api.entity.UserInfo;

import java.util.List;

/**
 * @Author: PowerYang
 * @Date: 2019/7/9 17:59
 */
@RpcClient
public interface UserService {
    UserInfo insertUserInfo(UserInfo userInfo);
    UserInfo getUserInfoById(long id);
    void deleteUserInfoById(long id);
    String getNameById(long id);
    List<UserInfo> getAllUser();
}
