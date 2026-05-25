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

    public static final String ID_BLANK_MESSAGE = "Id must not be blank";

    public static final String ORIGIN_BLANK_MESSAGE = "Origin must not be blank";
    public static final String ORIGIN_SIZE_MESSAGE = "Origin length must be 2 to 50 characters";
    public static final int ORIGIN_MIN = 2;
    public static final int ORIGIN_MAX = 50;

    public static final String DESTINATION_BLANK_MESSAGE = "Destination must not be blank";
    public static final String DESTINATION_SIZE_MESSAGE = "Destination length must be 2 to 50 characters";
    public static final int DESTINATION_MIN = 2;
    public static final int DESTINATION_MAX = 50;

    public static final String DATE_BLANK_MESSAGE = "Date must not be blank";

    public static final String SEATS_NULL_MESSAGE = "Seats must not be null";
    public static final String SEATS_MIN_MESSAGE = "Seats must be at least 1";
    public static final String SEATS_MAX_MESSAGE = "Seats must not exceed 1000";
    public static final int SEATS_MIN = 1;
    public static final int SEATS_MAX = 1000;
}
