package com.sber.jukeBox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        System.setProperty("java.net.preferIPv4Stack" , "true");
        SpringApplication.run(Application.class, args);
    }


}
