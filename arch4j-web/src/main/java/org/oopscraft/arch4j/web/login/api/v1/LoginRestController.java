package org.oopscraft.arch4j.web.login.api.v1;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/login")
@RequiredArgsConstructor
@Tag(name = "login", description = "Login")
public class LoginRestController {
}
