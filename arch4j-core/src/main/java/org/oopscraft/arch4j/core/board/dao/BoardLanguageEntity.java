package org.oopscraft.arch4j.core.board.dao;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.language.LanguageEntity;
import org.oopscraft.arch4j.core.data.SystemFieldEntity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "core_board_language")
@IdClass(BoardLanguageEntity.Pk.class)
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardLanguageEntity extends SystemFieldEntity implements LanguageEntity {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Pk implements Serializable {
        private String boardId;
        private String language;
    }

    @Id
    @Column(name = "board_id", length = 32)
    private String boardId;

    @Id
    @Column(name = "language", length = 8)
    private String language;

    @Column(name = "board_name")
    private String boardName;

    @Column(name = "message")
    @Lob
    private String message;

}

