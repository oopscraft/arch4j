package org.oopscraft.arch4j.core.common.support.texttable.column;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class GenericColumnParser extends ColumnParser {

    @Override
    boolean support(Object data) {
        return data instanceof Object;
    }

    @Override
    public List<String> getColumnNames(Object data) {
        List<String> columnNames = new ArrayList<>();
        Class<?> type = data.getClass();
        for (Class<?> clazz = type; clazz != null; clazz = clazz.getSuperclass()) {
            for(Field field : clazz.getDeclaredFields()){
                columnNames.add(field.getName());
            }
        }
        return columnNames;
    }

    @Override
    public Object getColumnValue(Object data, String columnName) {
        Object columnValue = null;
        Class<?> currentClass = data.getClass();
        do {
            try {
                Method getterMethod = currentClass.getDeclaredMethod("get" + toPascalCase(columnName));
                if(!getterMethod.canAccess(data)){
                    getterMethod.setAccessible(true);
                }
                columnValue = getterMethod.invoke(data);
                break;
            }catch(Throwable t){
                try {
                    Field field = currentClass.getDeclaredField(columnName);
                    if(!field.canAccess(data)){
                        field.setAccessible(true);
                    }
                    columnValue = field.get(data);
                    break;
                }catch(Throwable ignore){}
            }
        } while((currentClass = currentClass.getSuperclass()) != null);
        return columnValue;
    }
}
