package org.oopscraft.arch4j.web.security;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oopscraft.arch4j.web.WebProperties;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class SecurityTokenService {

    private final WebProperties webProperties;

    public String encodeSecurityToken(UserDetails userDetails) {
        JwtBuilder jwtBuilder = Jwts.builder();
        jwtBuilder.claim("username", userDetails.getUsername());
        if(webProperties.getSecurityExpireMinutes() > 0) {
            jwtBuilder.setExpiration(Date.from(ZonedDateTime.now().plusMinutes(webProperties.getSecurityExpireMinutes()).toInstant()));
        }
        jwtBuilder.signWith(SignatureAlgorithm.HS256, webProperties.getSecuritySigningKey());
        jwtBuilder.compressWith(CompressionCodecs.GZIP);
        return jwtBuilder.compact();
    }

    public UserDetails decodeSecurityToken(String authenticationToken) {
        Claims claims = Jwts.parser()
                .setSigningKey(webProperties.getSecuritySigningKey())
                .parseClaimsJws(authenticationToken).getBody();
        return UserDetailsImpl.builder()
                .username((String)claims.get("username"))
                .build();
    }

}
