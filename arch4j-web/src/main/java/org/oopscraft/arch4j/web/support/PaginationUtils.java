package org.oopscraft.arch4j.web.support;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class PaginationUtils {

    /**
     * toContentRange
     * @param name unit name
     * @param pageable pageable
     * @param page result page
     * @return
     */
    public static String toContentRange(String unit, Pageable pageable, Page page) {
        return new StringBuffer()
                .append(unit)
                .append(" ")
                .append(pageable.getOffset())
                .append("-")
                .append(pageable.getOffset() + pageable.getPageSize())
                .append("/")
                .append(page.getTotalElements())
                .toString();
    }

}
