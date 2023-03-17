package org.oopscraft.arch4j.core.support.texttable.column;

import java.util.ArrayList;
import java.util.List;

public class ArrayColumnParser extends ColumnParser {

    @Override
    boolean support(Object data) {
        return data.getClass().isArray();
    }

    @Override
    public List<String> getColumnNames(Object data) {
        List<String> columnNames = new ArrayList<>();
        if(data.getClass().isArray()) {
            Object[] array = (Object[]) data;
            for(int i = 0; i < array.length; i ++){
                columnNames.add(String.valueOf(i));
            }
        }
        return columnNames;
    }

    @Override
    public Object getColumnValue(Object data, String columnName) {
        if(data.getClass().isArray()) {
            Object[] array = (Object[]) data;
            return array[Integer.parseInt(columnName)];
        }
        return null;
    }
}
