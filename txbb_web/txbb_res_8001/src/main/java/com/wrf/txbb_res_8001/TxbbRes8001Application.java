package com.wrf.txbb_res_8001;

import com.github.tobato.fastdfs.FdfsClientConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@EnableEurekaClient
@Import(FdfsClientConfig.class)
@EnableFeignClients(basePackages = "com.wrf.txbb_web_api.feign")
public class TxbbRes8001Application {

    public static void main(String[] args) {
        SpringApplication.run(TxbbRes8001Application.class, args);
    }

}
