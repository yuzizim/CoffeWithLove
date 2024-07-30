package com.group7.cafemanagementsystem.Security;

public class JwtConstants {
    public static final String SECRET_KEY = "zzKuADl6R1vgbGYESb7meWuGvONSRgJ0Ps3rJZkP1HiYCpnsqu3BcZY2usMPxI8A";
    public static final long EXPIRE_DATE = 24 * 60 * 60 * 1000;
    public static final String BEARER = "BEARER ";
    public static final String JWT_COOKIE_NAME = "token";
    public static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";
    public static final long REFRESH_EXPIRE_DATE = 30 * 24 * 60 * 60 * 1000;
}
