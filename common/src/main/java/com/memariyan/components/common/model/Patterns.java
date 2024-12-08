package com.memariyan.components.common.model;

import java.util.regex.Pattern;

public class Patterns {

    public static final String EMAIL = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    public static final String PASSWORD = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$";
    public static final String PHONE_NUMBER = "^09\\d{9}$";

    public static boolean isValidEmail(String email) {
        return Pattern.matches(EMAIL, email);
    }

    public static boolean isValidPassword(String password) {
        return Pattern.matches(PASSWORD, password);
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        return Pattern.matches(PHONE_NUMBER, phoneNumber);
    }
}
