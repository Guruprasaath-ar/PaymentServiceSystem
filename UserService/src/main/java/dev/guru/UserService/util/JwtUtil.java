package dev.guru.UserService.util;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${jwt.secret.key}")
    private String SECRET_KEY;

    private final Long ACCESS_TOKEN_EXPIRY_MS =  15*60*1000L;
    private final Long REFRESH_TOKEN_EXPIRY_MS = 30*24*60*60*1000L;

    public Long getAccessTokenExpiryInMs() {
        return ACCESS_TOKEN_EXPIRY_MS;
    }

    public Long getRefreshTokenExpiryInMs() {
        return REFRESH_TOKEN_EXPIRY_MS;
    }

    public Long getAccessTokenExpireInSeconds() {
        return ACCESS_TOKEN_EXPIRY_MS/1000;
    }

    public Long getRefreshTokenExpireInSeconds() {
        return REFRESH_TOKEN_EXPIRY_MS/1000;
    }

    private Key getSigningKey(){
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String generateJwtToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRY_MS))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    public Claims getClaimsFromToken(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T extractClaimFromToken(String token, Function<Claims,T> claimsResolver){
        Claims claims = getClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public String getSubjectFromToken(String token){
        return extractClaimFromToken(token,Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token){
        return extractClaimFromToken(token,Claims::getExpiration);
    }

    public boolean isTokenExpired(String token){
        Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public boolean validateToken(String token,String username){
        return (!isTokenExpired(token) && username.equals(getSubjectFromToken(token)));
    }
}
