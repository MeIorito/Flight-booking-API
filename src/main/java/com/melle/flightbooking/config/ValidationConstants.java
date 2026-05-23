package com.melle.flightbooking.config;

public final class ValidationConstants {
    public static final String PASSWORD_BLANK_MESSAGE = "Password must not be blank";
    public static final String PASSWORD_SIZE_MESSAGE = "Password length must be 8 to 20 characters";
    public static final String PASSWORD_REGEXP_MESSAGE = "Password must contain at least one lowercase letter, one uppercase letter, and one number";
    public static final String  PASSWORD_REGEXP = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$";
    public static final int PASSWORD_MIN = 8;
    public static final int PASSWORD_MAX = 20;

    public static final String EMAIL_BLANK_MESSAGE = "Email must not be blank";
    public static final String EMAIL_VALID_MESSAGE = "Email must be valid";

    public static final String USERNAME_BLANK_MESSAGE = "Username must not be blank";
    public static final String USERNAME_SIZE_MESSAGE = "Username length must be 3 to 12 characters";
    public static final String USERNAME_REGEXP_MESSAGE = "Username may only contain letters, numbers, and underscores";
    public static final String  USERNAME_REGEXP = "^[a-zA-Z0-9_]+$";
    public static final int USERNAME_MIN = 3;
    public static final int USERNAME_MAX = 12;
}
