package com.se114p12.backend.services.general;

import com.se114p12.backend.exceptions.ResourceNotFoundException;
import com.se114p12.backend.exceptions.SMSException;
import com.se114p12.backend.repositories.authentication.UserRepository;
import com.twilio.Twilio;
import com.twilio.rest.lookups.v2.PhoneNumber;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SMSService {
  @Value("${twilio.account.sid}")
  private String TWILIO_ACCOUNT_SID;

  @Value("${twilio.auth.token}")
  private String TWILIO_AUTH_TOKEN;

  @Value("${twilio.verify.sid}")
  private String VERIFY_SERVICE_SID;

  private final UserRepository userRepository;

  @PostConstruct
  public void init() {
    Twilio.init(TWILIO_ACCOUNT_SID, TWILIO_AUTH_TOKEN);
  }

  public void sendOtp(String phoneNumber) {
    try {
      if (!userRepository.existsByPhone(phoneNumber)) {
        throw new ResourceNotFoundException("User not found");
      }
      Verification verification =
          Verification.creator(VERIFY_SERVICE_SID, phoneNumber, "sms").create();
      System.out.println("Send OTP to: " + phoneNumber);
      if (verification.getSid() == null) {
        throw new SMSException("Failed to send OTP");
      }
    } catch (Exception e) {
      throw new SMSException(e.getMessage());
    }
  }

  public boolean verifyOtp(String phoneNumber, String code) {
    try {
      VerificationCheck verificationCheck =
          VerificationCheck.creator(VERIFY_SERVICE_SID).setTo(phoneNumber).setCode(code).create();
      return "approved".equalsIgnoreCase(verificationCheck.getStatus());
    } catch (Exception e) {
      throw new SMSException(e.getMessage());
    }
  }

  public boolean lookupPhoneNumber(String phoneNumber) {
    try {
      phoneNumber = formatPhoneNumber(phoneNumber);
      PhoneNumber phoneInfo =
          PhoneNumber.fetcher(phoneNumber).setFields("line_type_intelligence").fetch();
      boolean isValid = phoneInfo.getValid();
      if (!isValid) {
        throw new SMSException("Invalid phone number");
      }
      System.out.println("Phone number: " + phoneInfo.getPhoneNumber());
      System.out.println("type: " + phoneInfo.getLineTypeIntelligence().get("type"));
      System.out.println(
          "Carrier name: " + phoneInfo.getLineTypeIntelligence().get("carrier_name"));
      System.out.println("Country: " + phoneInfo.getCountryCode());

      return "mobile".equalsIgnoreCase(phoneInfo.getLineTypeIntelligence().get("type").toString());
    } catch (Exception e) {
      throw new SMSException(e.getMessage());
    }
  }

  public static String formatPhoneNumber(String phoneNumber) {
    // Remove all non-numeric characters
    phoneNumber = phoneNumber.replaceAll("[^0-9]", "");

    if (phoneNumber.startsWith("0")) {
      return "+84" + phoneNumber.substring(1);
    } else if (phoneNumber.startsWith("84")) {
      return "+" + phoneNumber;
    } else {
      return phoneNumber;
    }
  }
}
