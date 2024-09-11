package org.oopscraft.arch4j.core.common.support.texttable;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestLine;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class GenericTextTable extends TextTable {

    private final Object object;

    /**
     * render
     * @return
     */
    String render() {
        StringBuffer sb = new StringBuffer();
        AsciiTable table = createAsciiTable();
        CWC_LongestLine cwc = new CWC_LongestLine();
        cwc.add(20,40).add(60,120);
        table.getRenderer().setCWC(cwc);
        table.addRule();
        table.addRow("Variable", "Value");
        table.addRule();
        List<String> columnNames = parseColumnNames(object);
        if(columnNames.isEmpty()){
            table.addRow(null, "(empty)");
            table.addRule();
        }else {
            for (String columnName : columnNames) {
                Object columnValue = getColumnValue(object, columnName);
                table.addRow(columnName, columnValue);
                table.addRule();
            }
        }
        return table.render();
    }

}
