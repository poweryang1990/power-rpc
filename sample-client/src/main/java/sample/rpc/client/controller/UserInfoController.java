package sample.rpc.client.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rpc.sample.api.entity.UserInfo;
import rpc.sample.api.service.UserService;

import java.util.List;

/**
 * @Author: PowerYang
 * @Date: 2019/7/11 16:22
 */
@RestController
@RequestMapping("user")
public class UserInfoController {

    @Autowired
    protected UserService userService;

    @GetMapping("list")
    public List<UserInfo> getAllUser(){
        return userService.getAllUser();
    }

}
