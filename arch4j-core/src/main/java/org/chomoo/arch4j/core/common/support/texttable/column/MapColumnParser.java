package org.chomoo.arch4j.core.common.support.texttable.column;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapColumnParser extends ColumnParser<Map> {

    @Override
    boolean support(Object data) {
        return data instanceof Map;
    }

    @Override
    public List<String> getColumnNames(Map data) {
        List<String> columnNames = new ArrayList<>();
        for (Object o : data.keySet()) {
            columnNames.add(String.valueOf(o));
        }
        return columnNames;
    }

    @Override
    public Object getColumnValue(Map data, String columnName) {
        return data.get(columnName);
    }

}
