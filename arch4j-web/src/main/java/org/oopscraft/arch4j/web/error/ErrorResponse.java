package org.oopscraft.arch4j.web.error;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Builder
@Getter
public class ErrorResponse {

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    private HttpStatus status;

    private String error;

    private String message;

    private String path;

}
