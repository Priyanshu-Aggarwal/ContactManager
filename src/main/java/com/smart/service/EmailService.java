
package com.smart.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;
    
    public boolean sendEmail(String to,String message,String Subject)
    {
        boolean f=false;
        SimpleMailMessage simpleMailMessage =new SimpleMailMessage();
        simpleMailMessage.setFrom("himanshiaggarwal232@gmail.com");
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(Subject);
        simpleMailMessage.setText(message);
        
        javaMailSender.send(simpleMailMessage);
        f=true;
        return f;
    }
    
}
