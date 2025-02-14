package com.example.HotelManagement.service.impl;

import com.example.HotelManagement.service.interfac.IEmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;


@Service
public class EmailService implements IEmailService {
    @Autowired
    private JavaMailSender mailSender;


    private Logger logger= LoggerFactory.getLogger(EmailService.class);
    @Override
    public void sendEmail(String to, String subject, String message) {
        SimpleMailMessage simpleMailMessage=new SimpleMailMessage();
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(message);
        simpleMailMessage.setFrom("raazkush028@gmail.com");
        mailSender.send(simpleMailMessage);
        logger.info("Email has been sent..");

    }

    @Override
    public void sendEmail(String[] to, String subject, String message) {
        SimpleMailMessage simpleMailMessage=new SimpleMailMessage();
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(message);
        simpleMailMessage.setFrom("raazkush028@gmail.com");
        mailSender.send(simpleMailMessage);
//        logger.info("Email hss been sent successfully..");

    }

    @Override
    public void sendEmailWithHtml(String to, String subject, String htmlContent) {
        MimeMessage simpleMailMessage=mailSender.createMimeMessage();
        try{
            MimeMessageHelper helper=new MimeMessageHelper(simpleMailMessage,true,"UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom("raazkush028@gmail.com");
            helper.setText(htmlContent,true);
            mailSender.send(simpleMailMessage);
            logger.info("Email has been sent..");
        }catch (MessagingException e){
            throw new RuntimeException(e);

        }
    }

    @Override
    public void sendEmailWithFile(String to, String subject, File file) {
    MimeMessage mimeMessage=mailSender.createMimeMessage();
    try{
        MimeMessageHelper helper=new MimeMessageHelper(mimeMessage,true);
        helper.setFrom("raazkush028@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);
        FileSystemResource fileSystemResource=new FileSystemResource(file);
        helper.addAttachment(fileSystemResource.getFilename(),file);
        mailSender.send(mimeMessage);
        logger.info("Email has been sent..");
    }catch (MessagingException e){
        throw new RuntimeException(e);
        }
    }
}
