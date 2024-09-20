package com.saas.user;

import com.saas.user.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class UserApplication {

    @Autowired
    private EmailService emailService;

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }

//    @Bean
//    public CommandLineRunner sendTestEmail() {
//        return args -> {
//            emailService.sendOtpMessage("ttdragon@yahoo.com", "Test Subject", "This is a test email from Spring Boot.");
//            System.out.println("Test email sent.");
//        };
//    }
}
