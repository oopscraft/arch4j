package org.oopscraft.arch4j.web.login.api.v1;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.security.model.User;
import org.oopscraft.arch4j.core.security.service.UserService;
import org.oopscraft.arch4j.web.login.api.v1.dto.LoginRequest;
import org.oopscraft.arch4j.web.login.api.v1.dto.LoginResponse;
import org.oopscraft.arch4j.web.security.service.SecurityTokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@RestController
@RequestMapping("api/v1/login")
@RequiredArgsConstructor
@Tag(name = "login", description = "Login")
public class LoginRestController {

    private final UserService userService;

    private final UserDetailsService userDetailsService;

    private final SecurityTokenService securityTokenService;

    private final AuthenticationFailureHandler authenticationFailureHandler;

    private final HttpServletRequest request;

    private final HttpServletResponse response;

    @PostMapping
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) throws IOException, ServletException {
        // checks username, password
        try {
            User user = userService.getUserByUsername(loginRequest.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("user not found"));
            boolean passwordMatched = userService.isPasswordMatched(user.getUserId(), loginRequest.getPassword());
            if (!passwordMatched) {
                throw new BadCredentialsException("bad credentials");
            }
        } catch (AuthenticationException e) {
            authenticationFailureHandler.onAuthenticationFailure(request, response, e);
            return null;
        }

        // generates security token (access, refresh token)
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
        String accessToken = securityTokenService.encodeSecurityToken(userDetails, 10);
        String refreshToken = securityTokenService.encodeSecurityToken(userDetails, 60*24*7);
        LoginResponse loginResponse = LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        return ResponseEntity.ok(loginResponse);
    }

    @GetMapping("status")
    public ResponseEntity<Void> loginStatus(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
