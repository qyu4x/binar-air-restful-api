package com.binarair.binarairrestapi.util;

import com.binarair.binarairrestapi.config.JwtTokenConfiguration;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtil {

    private final static Logger log = LoggerFactory.getLogger(JwtTokenUtil.class);

    private final JwtTokenConfiguration jwtTokenConfiguration;

    @Autowired
    public JwtTokenUtil(JwtTokenConfiguration jwtTokenConfiguration) {
        this.jwtTokenConfiguration = jwtTokenConfiguration;
    }

    public String getUsernameFromToken(String token) {
        return getClaimsFromToken(token, Claims::getSubject);
    }

    private <T> T getClaimsFromToken(String token, Function<Claims, T> claimsTFunction) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsTFunction.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(jwtTokenConfiguration.getSecretKey())
                .build()
                .parseClaimsJws(token).getBody();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        String usernameFromToken = getUsernameFromToken(token);
        return usernameFromToken.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        final Date expDate = getExpDateFromToken(token);
        return expDate.before(new Date());
    }

    private Date getExpDateFromToken(String token) {
        return getClaimsFromToken(token, Claims::getExpiration);
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }

    public String doGenerateToken(Map<String, Object> claims, String email) {
        log.info("subject token {} ", email);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(jwtTokenConfiguration.getTokenExpirationAfterDay())))
                .signWith(jwtTokenConfiguration.getSecretKey())
                .compact();

    }
}
