package org.oopscraft.arch4j.core.board.dao;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.common.data.BaseEntity;
import org.oopscraft.arch4j.core.common.i18n.I18nEntity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "core_board_i18n")
@IdClass(BoardI18nEntity.Pk.class)
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardI18nEntity extends BaseEntity implements I18nEntity {

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

    @Column(name = "name")
    private String name;

    @Column(name = "message")
    @Lob
    private String message;

}

