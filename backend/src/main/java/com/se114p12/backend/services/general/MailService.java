package com.se114p12.backend.services.general;

import static com.se114p12.backend.constants.AppConstant.BACKEND_URL;

import com.se114p12.backend.exceptions.BadRequestException;
import jakarta.mail.internet.MimeMessage;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.CharEncoding;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class MailService {

  private final JavaMailSender mailSender;
  private final TemplateEngine templateEngine;

  @Async
  public void sendEmail(
      String to, String subject, String content, boolean isMultipart, boolean isHtml) {
    // Prepare message using a Spring helper
    MimeMessage mimeMessage = mailSender.createMimeMessage();
    try {
      MimeMessageHelper message =
          new MimeMessageHelper(mimeMessage, isMultipart, CharEncoding.UTF_8);
      message.setTo(to);
      //            message.setFrom("");
      message.setSubject(subject);
      message.setText(content, isHtml);
      mailSender.send(mimeMessage);
    } catch (Exception e) {
      throw new BadRequestException(e.getMessage());
    }
  }

  @Async
  public void sendEmailFromTemplate(
      String to, String subject, String templateName, Map<String, Object> model) {
    Context context = new Context();
    context.setVariables(model);
    String content = templateEngine.process(templateName, context);
    sendEmail(to, subject, content, false, true);
  }

  @Async
  public void sendActivationEmail(String email, String activationCode) {
    Map<String, Object> model =
        Map.of(
            "verifyLink",
            BACKEND_URL + "/auth/verify-email?code=" + activationCode,
            "email",
            email);
    sendEmailFromTemplate(email, "Măm - Xác thực email của bạn", "mail/verify-email", model);
  }

  @Async
  public void sendOtpEmail(String email, String otp) {
    Map<String, Object> model = Map.of("otp", otp);
    sendEmailFromTemplate(email, "Măm - Mã xác thực của bạn", "mail/send-otp", model);
  }
}
