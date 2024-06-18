package com.popo.todolist.component;

import com.popo.todolist.entity.UserEntity;
import com.popo.todolist.model.response.TokenResponseDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;

import static com.popo.todolist.common.constants.JwtValueType.*;

@Slf4j
@Component
public class JwtUtil {
    private Key key;
    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.accessToken_exp_time}")
    private long accessTokenExpTime;
    @Value("${jwt.refreshToken_exp_time}")
    private long refreshTokenExpireTime;

    public JwtUtil() {
    }

    @PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenResponseDto createToken(UserEntity userEntity) {
        return createJwtString(userEntity.getId(), userEntity.getNickName(), userEntity.getEmail());
    }

    public TokenResponseDto refreshAccessToken(UserEntity userEntity) {
        return create(userEntity.getId(), userEntity.getNickName(), userEntity.getEmail());
    }

    private TokenResponseDto create(Long id, String nickName, String email) {
        Claims claims = getClaims(id, nickName, email);
        ZonedDateTime now = ZonedDateTime.now();
        String accessToken = getToken(claims, now, now.plusSeconds(accessTokenExpTime));
        return new TokenResponseDto(accessToken);
    }


    private TokenResponseDto createJwtString(Long id, String nickName, String email) {
        Claims claims = getClaims(id, nickName, email);
        ZonedDateTime now = ZonedDateTime.now();
        String accessToken = getToken(claims, now, now.plusSeconds(accessTokenExpTime));
        String refreshToken = getToken(claims, now, now.plusSeconds(refreshTokenExpireTime));

        return new TokenResponseDto(accessToken, refreshToken);
    }

    private Claims getClaims(Long id, String nickName, String email) {
        Claims claims = Jwts.claims();
        claims.put(ID.toString(), id);
        claims.put(NICK_NAME.toString(), nickName);
        claims.put(EMAIL.toString(), email);
        return claims;
    }


    public Long extractUserId(String token) {
        return extractAllClaims(token).get(ID.toString(), Long.class);
    }

    public String extractNickName(String token) {
        return extractAllClaims(token).get(NICK_NAME.toString(), String.class);
    }

    public String extractEmail(String token) {
        return extractAllClaims(token).get(EMAIL.toString(), String.class);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    private String getToken(Claims claims, ZonedDateTime now, ZonedDateTime tokenValidity) {
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(now.toInstant()))
                .setExpiration(Date.from(tokenValidity.toInstant()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            // TODO KST 의도적인 예외 처리 필요
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }


}
