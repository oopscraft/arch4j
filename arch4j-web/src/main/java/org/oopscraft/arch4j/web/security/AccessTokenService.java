package org.oopscraft.arch4j.web.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.ZonedDateTime;
import java.util.Date;

@Service
public class AccessTokenService {

    private static final String CLAIM_NAME = "userDetails";

    private static final String ACCESS_TOKEN_COOKIE_NAME = "ACCESS_TOKEN";

    private static final String ACCESS_TOKEN_HEADER_NAME = "X-ACCESS-TOKEN";

    private String secretKey = "test";

    private int timeout = 60;

    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * encode
     * @param userDetails
     * @param expireMinutes
     * @return
     */
    public String encode(UserDetailsImpl userDetails, int expireMinutes) throws Exception {
        JwtBuilder jwtBuilder = Jwts.builder();
        jwtBuilder.claim(CLAIM_NAME, objectMapper.writeValueAsString(userDetails));
        if(expireMinutes > 0) {
            jwtBuilder.setExpiration(Date.from(ZonedDateTime.now().plusMinutes(expireMinutes).toInstant()));
        }
        jwtBuilder.signWith(SignatureAlgorithm.HS256, secretKey);
        jwtBuilder.compressWith(CompressionCodecs.GZIP);
        return jwtBuilder.compact();
    }

    /**
     * decode
     * @param jwt
     * @return
     */
    public UserDetailsImpl decode(String jwt) throws Exception {
        Claims claims = Jwts.parser()
                .setSigningKey(this.secretKey)
                .parseClaimsJws(jwt).getBody();
        String userDetailsJson = (String) claims.get(CLAIM_NAME);
        return objectMapper.readValue(userDetailsJson, UserDetailsImpl.class);
    }


    /**
     * issueAccessToken
     * @param userDetails
     * @param response
     */
    public void issueAccessToken(UserDetailsImpl userDetails, HttpServletResponse response) {
        try {
            String accessToken = encode(userDetails, timeout);
            response.setHeader(ACCESS_TOKEN_HEADER_NAME, accessToken);
            Cookie accessTokenCookie = new Cookie(ACCESS_TOKEN_COOKIE_NAME, accessToken);
            accessTokenCookie.setPath("/");
            accessTokenCookie.setHttpOnly(true);
            accessTokenCookie.setMaxAge(timeout * 60);
            response.addCookie(accessTokenCookie);
        }catch(Throwable t){
            throw new RuntimeException(t);
        }
    }

    /**
     * expireAccessToken
     * @param request
     * @param response
     */
    public void expireAccessToken(HttpServletRequest request, HttpServletResponse response) {
        if(request.getCookies() != null) {
            for(Cookie cookie : request.getCookies()) {
                if(ACCESS_TOKEN_COOKIE_NAME.equals(cookie.getName())) {
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
