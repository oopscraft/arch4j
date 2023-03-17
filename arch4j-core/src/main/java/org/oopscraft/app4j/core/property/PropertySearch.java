package org.oopscraft.app4j.core.property;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Role search condition
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropertySearch {

    private String id;

    private String name;

    private String value;

}
