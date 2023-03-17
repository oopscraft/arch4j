package org.oopscraft.arch4j.core.support.texttable.column;

import java.util.ArrayList;
import java.util.List;

public abstract class ColumnParser<T> {

    private static List<ColumnParser> columnParserRegistries = new ArrayList<>() {{
        add(new MapColumnParser());
        add(new ArrayColumnParser());
        add(new GenericColumnParser());
    }};

    /**
     * valueOf
     * @param data
     * @return
     */
    public static ColumnParser valueOf(Object data) {
        for(ColumnParser columnParser : columnParserRegistries) {
            if(columnParser.support(data)){
                return columnParser;
            }
        }
        throw new RuntimeException("Unsupported column type");
    }

    abstract boolean support(Object data);

    public abstract List<String> getColumnNames(T data);

    public abstract Object getColumnValue(T data, String columnName);

    /**
     * Checks if delimiter char
     * @param c
     * @return
     */
    static boolean isDelimiterChar(char c) {
        if(c == ' '	|| c == '-' || c == '_'){
            return true;
        }else {
            return false;
        }
    }

    /**
     * Convert string into camel case notation.
     * @param name
     * @return
     */
    static String toCamelCase(String name) {

        char[] chars = name.toCharArray();
        StringBuffer convertedName = new StringBuffer();
        int convertedNameLen = 0;
        for(int idx = 0; idx < chars.length; idx ++ ) {
            boolean isCamelCase = false;
            char previousChar = idx == 0 ? '\0' : chars[idx-1];
            char currentChar = chars[idx];

            // Checks camel case.
            if(isDelimiterChar(previousChar)) {
                isCamelCase = true;
            }

            // Checks skip chars.
            if(isDelimiterChar(currentChar)) {
                continue;
            }

            // Checks already CamelCase.
            if(Character.isLowerCase(previousChar)
                && Character.isUpperCase(currentChar)){
                isCamelCase = true;
            }

            // Appends char into camelCase StringBuffer.
            if(convertedNameLen != 0 && isCamelCase) {
                convertedName.append(String.valueOf(currentChar).toUpperCase());
            }else{
                convertedName.append(String.valueOf(currentChar).toLowerCase());
            }

            // Increases count.
            convertedNameLen ++;
        }

        return convertedName.toString();
    }

    /**
     * Convert string into Pascal Case notation.
     * @param name
     * @return
     */
    static String toPascalCase(String name) {

        char[] chars = name.toCharArray();
        StringBuffer convertedName = new StringBuffer();
        for(int idx = 0; idx < chars.length; idx ++ ) {
            boolean isPascalCase = false;
            char previousChar = idx == 0 ? '\0' : chars[idx-1];
            char currentChar = chars[idx];

            // Checks camel case.
            if(isDelimiterChar(previousChar)) {
                isPascalCase = true;
            }

            // Checks skip chars.
            if(isDelimiterChar(currentChar)) {
                continue;
            }

            // Checks already CamelCase.
            if(Character.isLowerCase(previousChar) && Character.isUpperCase(currentChar)){
                isPascalCase = true;
            }

            if(idx == 0) {
                isPascalCase = true;
            }

            // Appends char into camelCase StringBuffer.
            if(isPascalCase) {
                convertedName.append(String.valueOf(currentChar).toUpperCase());
            }else{
                convertedName.append(String.valueOf(currentChar).toLowerCase());
            }
        }

        return convertedName.toString();
    }


}
