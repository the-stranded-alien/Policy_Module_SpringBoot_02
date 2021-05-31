package SpringBoot.Policy_Module_Ultimate.services.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendMail(String to, String body, String topic)
    {
        System.out.println("Sending Mail...");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("sritt4688665@gmail.com");
        message.setTo(to);
        message.setText(body);
        message.setSubject(topic);
        javaMailSender.send(message);
        System.out.println("Mail Sent!");
    }
}
