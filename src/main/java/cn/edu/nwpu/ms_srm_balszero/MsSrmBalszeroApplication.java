package cn.edu.nwpu.ms_srm_balszero;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class MsSrmBalszeroApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsSrmBalszeroApplication.class, args);
    }

}
