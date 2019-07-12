package sample.rpc.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import power.rpc.server.annotation.EnableRpcServer;


/**
 * @Author: PowerYang
 * @Date: 2019/7/2 10:48
 */
@SpringBootApplication
//指定要扫描的包
@ComponentScan({"**.rpc.*","rpc.*"})
@EnableRpcServer
@Slf4j
public class SampleRpcServerApplication {

    public static void main(String[] args)  {
        SpringApplication.run(SampleRpcServerApplication.class, args);
    }
}
