package com.se114p12.backend.util;

import org.springframework.stereotype.Service;

@Service
public class TypeUtil {
    public static final String PHONE_REGEX = "^(03|05|07|08|09)[0-9]{8}$\n";
    public static  final String EMAIL_REGEX = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$\n";

    public static int checkUsernameType(String username) {
        if (username.matches(PHONE_REGEX)) {
            return 1;
        } else if (username.matches(EMAIL_REGEX)) {
            return 2;
        } else {
            return 3;
        }
    }
}
