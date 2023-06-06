package org.oopscraft.arch4j.core.security;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.ZonedDateTime;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationTokenService {

    @Value("${core.security.authentication-token.signing-key}")
    private String signingKey;

    @Value("${core.security.authentication-token.expire-minutes}")
    private Integer expireMinutes;

    /**
     * encode
     * @param userDetails userDetailsImpl
     * @return access token
     */
    public String encodeAuthenticationToken(UserDetails userDetails) {
        JwtBuilder jwtBuilder = Jwts.builder();
        jwtBuilder.claim("username", userDetails.getUsername());
        if(expireMinutes > 0) {
            jwtBuilder.setExpiration(Date.from(ZonedDateTime.now().plusMinutes(expireMinutes).toInstant()));
        }
        jwtBuilder.signWith(SignatureAlgorithm.HS256, signingKey);
        jwtBuilder.compressWith(CompressionCodecs.GZIP);
        return jwtBuilder.compact();
    }

    /**
     * decode
     * @param authenticationToken access token
     * @return userDetails
     */
    public UserDetails decodeAuthenticationToken(String authenticationToken) {
        Claims claims = Jwts.parser()
                .setSigningKey(signingKey)
                .parseClaimsJws(authenticationToken).getBody();
        return UserDetailsImpl.builder()
                .username((String)claims.get("username"))
                .build();
    }

    public String parseAuthenticationToken(HttpServletRequest request) {
        String authenticationToken = parseAuthenticationTokenFromHeader(request);
        if(authenticationToken == null) {
            authenticationToken = parseAuthenticationTokenFromCookie(request);
        }
        return authenticationToken;
    }

    private String parseAuthenticationTokenFromHeader(HttpServletRequest request) {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(authorization != null && authorization.trim().length() > 0) {
            String[] authorizationArray = authorization.split(" ");
            if(authorizationArray.length >= 2) {
                return authorizationArray[1];
            }
        }
        return null;
    }

    private String parseAuthenticationTokenFromCookie(HttpServletRequest request) {
        if(request.getCookies() != null) {
            for(Cookie cookie : request.getCookies()) {
                if(HttpHeaders.AUTHORIZATION.equals(cookie.getName())){
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public void issueAuthenticationToken(HttpServletResponse response, String authenticationToken) {
        // set response header
        response.setHeader(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", authenticationToken));

        // set cookie
        Cookie cookie = new Cookie(HttpHeaders.AUTHORIZATION, authenticationToken);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(expireMinutes * 60);
        response.addCookie(cookie);
    }

    public void expireAuthenticationToken(HttpServletRequest request, HttpServletResponse response) {
        if(request.getCookies() != null) {
            for(Cookie cookie : request.getCookies()) {
                if(HttpHeaders.AUTHORIZATION.equals(cookie.getName())) {
                    cookie.setPath("/");
                    cookie.setValue("");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                    break;
                }
            }
        }
    }

}
