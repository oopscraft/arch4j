package org.oopscraft.arch4j.core.security;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

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

}
