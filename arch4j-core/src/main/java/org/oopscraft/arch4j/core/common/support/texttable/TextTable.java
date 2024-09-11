package org.oopscraft.arch4j.core.common.support.texttable;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciithemes.a7.A7_Grids;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;
import org.oopscraft.arch4j.core.common.support.texttable.column.ColumnParser;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public abstract class TextTable {

    abstract String render();

    /**
     * valueOf
     * @param data
     * @return
     */
    public static TextTable valueOf(Object data) {
        if(data == null){
            return new NullTextTable();
        }
        if(data.getClass().isArray()){
            return new ArrayTextTable((Object[]) data);
        }
        if(data instanceof Map){
            return new MapTextTable((Map) data);
        }
        if(data instanceof Collection) {
            return new CollectionTextTable((Collection) data);
        }
        return new GenericTextTable(data);
    }

    @Override
    public String toString() {
        try {
            return System.lineSeparator() + render();
        }catch(Throwable t){
            return t.getMessage();
        }
    }

    /**
     * createAsciiTable
     * @return
     */
    static AsciiTable createAsciiTable() {
        AsciiTable table = new AsciiTable();
        table.getContext().setGrid(A7_Grids.minusBarPlusEquals());
        table.setTextAlignment(TextAlignment.LEFT);
        return table;
    }

    /**
     * parseColumnNames
     * @param data
     * @return
     */
    static List<String> parseColumnNames(Object data) {
        ColumnParser columnParser = ColumnParser.valueOf(data);
        return columnParser.getColumnNames(data);
    }

    /**
     * getColumnValue
     * @param data
     * @param columnName
     * @return
     */
    static String getColumnValue(Object data, String columnName) {
        ColumnParser columnParser = ColumnParser.valueOf(data);
        Object columnValue = columnParser.getColumnValue(data, columnName);
        if(columnValue == null){
            return "null";
        }else{
            return columnValue.toString();
        }
    }

}
