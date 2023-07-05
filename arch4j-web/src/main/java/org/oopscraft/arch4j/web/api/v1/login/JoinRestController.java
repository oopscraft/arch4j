package org.oopscraft.arch4j.web.api.v1.login;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.email.EmailService;
import org.oopscraft.arch4j.core.email.EmailException;
import org.oopscraft.arch4j.core.verification.Verification;
import org.oopscraft.arch4j.core.verification.VerificationService;
import org.oopscraft.arch4j.web.api.v1.login.dto.CheckVerificationRequest;
import org.oopscraft.arch4j.web.api.v1.login.dto.CheckVerificationResponse;
import org.oopscraft.arch4j.web.api.v1.login.dto.IssueVerificationRequest;
import org.oopscraft.arch4j.web.api.v1.login.dto.IssueVerificationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.spring5.SpringTemplateEngine;

@RestController
@RequestMapping("api/v1/login/join")
@RequiredArgsConstructor
public class JoinRestController {

    private final VerificationService verificationService;

    private final EmailService emailService;

    private final SpringTemplateEngine templateEngine;

    /**
     * send verification
     * @param request request
     * @return response
     */
    @PostMapping("issue-verification")
    public ResponseEntity<IssueVerificationResponse> issueVerification(@RequestBody IssueVerificationRequest request) {

        // issue verification
        Verification verification = verificationService.issueVerification();

        // send email
        try {
            emailService.sendEmail(request.getEmail(), "인증요청", verification.getAnswer());
        } catch (EmailException e) {
            throw new RuntimeException(e);
        }

        // response
        IssueVerificationResponse response = IssueVerificationResponse.builder()
                .issueId(verification.getIssueId())
                .issuedAt(verification.getIssuedAt())
                .build();
        return ResponseEntity.ok(response);
    }

    /**
     * check verification
     * @param request request
     * @return response
     */
    @PostMapping("check-verification")
    public ResponseEntity<CheckVerificationResponse> checkVerification(@RequestBody CheckVerificationRequest request) {

        // check answer
        boolean result = verificationService.isCorrectAnswer(request.getIssueId(), request.getAnswer());

        // response
        CheckVerificationResponse response = CheckVerificationResponse.builder()
                .result(result)
                .build();
        return ResponseEntity.ok(response);
    }

}
