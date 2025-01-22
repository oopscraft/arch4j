package org.chomookun.arch4j.web.common.error;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;

@Builder
@Getter
public class ErrorResponse {

    @Builder.Default
    private Instant timestamp = Instant.now();

    private Integer status;

    private String error;

    private String message;

    private String path;

    /**
     * factory method
     * @param request http servlet request
     * @param status http status
     * @param message message
     * @return error response
     */
    public static ErrorResponse from(HttpServletRequest request, HttpStatus status, String message) {
        return ErrorResponse.builder()
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(request.getRequestURI())
                .build();
    }

}
