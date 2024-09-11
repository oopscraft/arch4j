package org.oopscraft.arch4j.core.common.support.texttable;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestLine;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ArrayTextTable extends TextTable {

    private final Object[] array;

    @Override
    String render() {
        StringBuffer sb = new StringBuffer();
        AsciiTable table = createAsciiTable();
        CWC_LongestLine cwc = new CWC_LongestLine();
        cwc.add(10,20).add(70,140);
        table.getRenderer().setCWC(cwc);
        table.addRule();
        table.addRow("Index", "Value");
        table.addRule();
        List<String> columnNames = parseColumnNames(array);
        if(array.length < 1){
            table.addRow(null, "(empty)");
            table.addRule();
        }else{
            for (String columnName : columnNames) {
                Object columnValue = getColumnValue(array, columnName);
                table.addRow(columnName, columnValue);
                table.addRule();
            }
        }
        return table.render();
    }
}
