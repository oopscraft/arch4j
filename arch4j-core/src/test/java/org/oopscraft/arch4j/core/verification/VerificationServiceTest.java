package org.oopscraft.arch4j.core.verification;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.oopscraft.arch4j.core.test.CoreTestSupport;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class VerificationServiceTest extends CoreTestSupport {

    private final VerificationService verificationService;

    @Test

    public void issueVerification() {
        Verification verification = verificationService.issueVerification();
        assertNotNull(verification.getIssueId());
        assertNotNull(verification.getAnswer());
    }

    @Test
    public void isCorrectAnswer() {
        Verification verification = verificationService.issueVerification();
        assertTrue(verificationService.isCorrectAnswer(verification.getIssueId(), verification.getAnswer()));
        assertFalse(verificationService.isCorrectAnswer(verification.getIssueId(), "INCORRECT_VALUE"));
    }

}