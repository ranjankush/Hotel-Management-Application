package com.example.HotelManagement.service.interfac;


import java.io.File;

public interface IEmailService {
     void sendEmail(String to, String subject, String message);

     void sendEmail(String []to,String subject,String message);

     void sendEmailWithHtml(String to,String subject, String htmlContent);

     void sendEmailWithFile(String to, String subject, File file);
}
