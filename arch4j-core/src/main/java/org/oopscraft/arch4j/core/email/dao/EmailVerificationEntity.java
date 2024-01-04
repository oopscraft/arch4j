package org.oopscraft.arch4j.core.email.dao;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
