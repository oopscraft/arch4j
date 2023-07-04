package org.oopscraft.arch4j.core.verification;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.data.IdGenerator;
import org.oopscraft.arch4j.core.verification.dao.VerificationEntity;
import org.oopscraft.arch4j.core.verification.dao.VerificationRepository;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VerificationService {

    private final VerificationRepository verificationRepository;

    /**
     * issue verification
     * @return verification
     */
    public Verification issueVerification() {
        String issueId = IdGenerator.uuid();
        String answer = generateAnswer();

        // save verification data
        VerificationEntity verificationEntity = VerificationEntity.builder()
                .issueId(issueId)
                .issuedAt(LocalDateTime.now())
                .answer(answer)
                .build();
        verificationEntity = verificationRepository.saveAndFlush(verificationEntity);
        return Verification.from(verificationEntity);
    }

    /**
     * generates answer
     * @return answer
     */
    private String generateAnswer() {
        SecureRandom random = new SecureRandom();
        int answer = 100000 + random.nextInt(89999);
        return String.valueOf(answer);
    }

    /**
     * return if correct answer
     * @param issueId issue id
     * @param answer answer
     * @return result
     */
    public boolean isCorrectAnswer(String issueId, String answer) {
        VerificationEntity verificationEntity = verificationRepository.findById(issueId).orElse(null);
        if(verificationEntity == null) {
            return false;
        }
        return verificationEntity.getAnswer().contentEquals(answer);
    }

}
