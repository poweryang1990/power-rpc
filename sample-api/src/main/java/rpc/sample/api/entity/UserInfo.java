package rpc.sample.api.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: PowerYang
 * @Date: 2019/7/9 17:58
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {

    private long id;
    private  String name;
    private  int  age;
}
