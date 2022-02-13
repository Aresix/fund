package com.fundMonitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author lli.chen
 */
@EnableScheduling
@SpringBootApplication
public class FundMonitorApplication {
    public static void main(String[] args) {
        SpringApplication.run(FundMonitorApplication.class);
    }
}
