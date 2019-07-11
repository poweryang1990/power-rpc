package sample.rpc.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import power.rpc.client.annotation.EnableRpcClients;

/**
 * @Author: PowerYang
 * @Date: 2019/7/10 15:11
 */
@SpringBootApplication
@ComponentScan({"**rpc.**"})
@EnableRpcClients({"rpc.sample.api"})
public class SampleRpcClientApplication {
    public static void main(String[] args)  {
        SpringApplication.run(SampleRpcClientApplication.class, args);
    }
}
