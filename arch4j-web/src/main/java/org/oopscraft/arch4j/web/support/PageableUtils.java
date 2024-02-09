package org.oopscraft.arch4j.web.support;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public class PageableUtils {

    public static String toContentRange(String unit, Pageable pageable, Number totalSize) {
        StringBuilder contentRange = new StringBuilder("");

        // unit
        contentRange.append(Optional.ofNullable(unit).orElse("unit"));

        // range
        contentRange.append(" ");
        if(pageable == null || pageable.isUnpaged()) {
            contentRange.append("*");
        }else{
            contentRange.append(pageable.getOffset())
                    .append("-")
                    .append(pageable.getOffset() + pageable.getPageSize());
        }

        // size
        contentRange.append("/");
        if(totalSize == null) {
            contentRange.append("*");
        }else{
            contentRange.append(totalSize);
        }

        return contentRange.toString();
    }

    public static String toContentRange(String unit, Pageable pageable) {
        return toContentRange(unit, pageable, null);
    }

}
