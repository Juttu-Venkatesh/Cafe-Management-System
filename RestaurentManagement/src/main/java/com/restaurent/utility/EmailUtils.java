//package com.restaurent.utility;
//
//import java.util.List;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.stereotype.Service;
//
//@Service
//public class EmailUtils {
//
//    @Autowired
//    private JavaMailSender emailSender;
//
//    public void sendSimpleMessage(String to, String subject, String text, List<String> ccList) {
//        if (to == null || subject == null || text == null) {
//            throw new IllegalArgumentException("Email fields cannot be null");
//        }
//
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom("your-email@gmail.com");
//        message.setTo(to);
//        message.setSubject(subject);
//        message.setText(text);
//
//        if (ccList != null && !ccList.isEmpty()) {
//            message.setCc(ccList.toArray(new String[0]));
//        }
//
//        emailSender.send(message);
//    }
//}
