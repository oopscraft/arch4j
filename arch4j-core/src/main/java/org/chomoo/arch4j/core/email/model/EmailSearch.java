package org.chomoo.arch4j.core.email.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailSearch {

    private String emailId;

    private String name;

}
