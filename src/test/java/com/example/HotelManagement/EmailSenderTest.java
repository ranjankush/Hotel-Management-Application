package com.example.HotelManagement;

import com.example.HotelManagement.service.impl.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = HotelManagementApplication.class)

public class EmailSenderTest {
    @Autowired
    private EmailService emailService;
    @Test
    void emailSendTest(){
        System.out.println("sending email");
        emailService.sendEmail("ranjankr916264@gmail.com","Email from HMS","This email send using Spring Boot Java MailSender technology Through HMSApplication");
    }
@Test
    void sendHtmlInEmail(){
        String html=" "+"<h1 style='color:red;border:1px solid red;'> Welcome To Taj Hotel </h1>"+" ";
        emailService.sendEmailWithHtml("ranjankr916264@gmail.com","Email From Taj",html);
    }

}
