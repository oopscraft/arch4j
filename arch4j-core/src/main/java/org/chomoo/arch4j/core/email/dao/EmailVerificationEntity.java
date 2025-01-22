package org.chomoo.arch4j.core.email.dao;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomoo.arch4j.core.common.data.BaseEntity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "core_email_verification")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailVerificationEntity extends BaseEntity {

    @Id
    @Column(name = "email", length = 64)
    private String email;

    @Column(name = "issued_at")
    private LocalDateTime issuedAt;

    @Column(name = "answer", length = 32)
    private String answer;

}
