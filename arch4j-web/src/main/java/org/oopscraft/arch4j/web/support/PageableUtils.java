package org.oopscraft.arch4j.web.support;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class PageableUtils {

    public static String toContentRange(String unit, Pageable pageable, long totalSize) {
        return new StringBuffer()
                .append(unit)
                .append(" ")
                .append(pageable.getOffset())
                .append("-")
                .append(pageable.getOffset() + pageable.getPageSize())
                .append("/")
                .append(totalSize)
                .toString();
    }

}
