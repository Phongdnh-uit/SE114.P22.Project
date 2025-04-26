package com.se114p12.backend.services.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class MailService {

  private final JavaMailSender emailSender;
  private final TemplateEngine templateEngine;

  @Async
  public void sendEmail(String to, String subject, String templateName, Model model)
      throws MessagingException {
    Context context = new Context();
    // context.setVariables(model.asMap());
    context.setVariable("name", "Phong");
    String body = templateEngine.process(templateName, context);

    System.out.println(body);

    MimeMessage message = emailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true);
    helper.setTo(to);
    helper.setSubject(subject);
    helper.setText(body, true);

    emailSender.send(message);
  }
}
