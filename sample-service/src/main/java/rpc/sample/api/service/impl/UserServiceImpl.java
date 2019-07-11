package rpc.sample.api.service.impl;


import power.rpc.annotation.RpcService;
import rpc.sample.api.entity.UserInfo;
import rpc.sample.api.service.UserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @Author: PowerYang
 * @Date: 2019/7/9 18:03
 */
@RpcService
public class UserServiceImpl implements UserService {

    private  static ConcurrentMap<Long,UserInfo> concurrentMap=new ConcurrentHashMap<Long, UserInfo>(){
        {
         put(Long.valueOf(1),new UserInfo(1,"power yang",28));
         put(Long.valueOf(2),new UserInfo(2,"power yang",28));
        }
    };
    @Override
    public UserInfo insertUserInfo(UserInfo userInfo) {
        concurrentMap.put(userInfo.getId(),userInfo);
        return userInfo;
    }

    @Override
    public UserInfo getUserInfoById(long id) {
        return concurrentMap.get(id);
    }

    @Override
    public void deleteUserInfoById(long id) {
        concurrentMap.remove(id);
    }

    @Override
    public String getNameById(long id) {
        UserInfo userInfo=concurrentMap.get(id);
        if (userInfo==null) {
            return "";
        }
        return userInfo.getName();
    }

    @Override
    public List<UserInfo> getAllUser() {
        return new ArrayList(concurrentMap.values());
    }
}
