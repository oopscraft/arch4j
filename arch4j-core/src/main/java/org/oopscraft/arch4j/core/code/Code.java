package org.oopscraft.arch4j.core.code;

import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Code {
	
	private String codeId;
	
	private String codeName;
	
	private String note;

    @Builder.Default
	private List<CodeItem> codeItems = new ArrayList<>();

}
