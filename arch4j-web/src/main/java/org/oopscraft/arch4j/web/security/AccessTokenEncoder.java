package org.oopscraft.arch4j.web.security;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oopscraft.arch4j.core.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccessTokenEncoder {

    @Value("${web.access-token.signing-key}")
    private String signingKey;

    @Value("${web.access-token.expire-minutes}")
    private Integer expireMinutes;

    /**
     * encode
     * @param userDetails userDetailsImpl
     * @return access token
     */
    public String encode(UserDetailsImpl userDetails) {
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
     * @param accessToken access token
     * @return userDetailsImpl
     */
    public UserDetailsImpl decode(String accessToken) {
        Claims claims = Jwts.parser()
                .setSigningKey(signingKey)
                .parseClaimsJws(accessToken).getBody();
        return UserDetailsImpl.builder()
                .username((String)claims.get("username"))
                .build();
    }

//    public String parseAccessToken(HttpServletRequest request) {
//        String accessToken = null;
//
//        // checks request header
//        accessToken = request.getHeader(SecurityConstant.ACCESS_TOKEN_NAME);
//
//        // checks cookie
//        if(accessToken == null || accessToken.trim().length() < 1) {
//            if(request.getCookies() != null) {
//                for(Cookie cookie : request.getCookies()) {
//                    if(SecurityConstant.ACCESS_TOKEN_NAME.equals(cookie.getName())) {
//                        accessToken = cookie.getValue();
//                        break;
//                    }
//                }
//            }
//        }
//
//        // return
//        return accessToken;
//    }
//
//    /**
//     * issueAccessToken
//     * @param userDetails
//     * @param response
//     */
//    public void issueAccessToken(UserDetailsImpl userDetails, HttpServletResponse response) {
//        String securityToken = encode(userDetails, SecurityConstant.ACCESS_TOKEN_VALID_MINUTES);
//        response.setHeader(SecurityConstant.ACCESS_TOKEN_NAME, securityToken);
//        Cookie securityTokenCookie = new Cookie(SecurityConstant.ACCESS_TOKEN_NAME, securityToken);
//        securityTokenCookie.setPath("/");
//        securityTokenCookie.setHttpOnly(true);
//        securityTokenCookie.setMaxAge(SecurityConstant.ACCESS_TOKEN_VALID_MINUTES * 60);
//        response.addCookie(securityTokenCookie);
//    }
//
//    /**
//     * expireAccessToken
//     * @param request
//     * @param response
//     */
//    public void expireAccessToken(HttpServletRequest request, HttpServletResponse response) {
//        if(request.getCookies() != null) {
//            for(Cookie cookie : request.getCookies()) {
//                if(SecurityConstant.ACCESS_TOKEN_NAME.equals(cookie.getName())) {
//                    cookie.setPath("/");
//                    cookie.setValue("");
//                    cookie.setMaxAge(0);
//                    response.addCookie(cookie);
//                    break;
//                }
//            }
//        }
//    }

}
