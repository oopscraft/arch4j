package org.oopscraft.arch4j.core.common.support.texttable;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestLine;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class MapTextTable extends TextTable {

    private final Map map;

    @Override
    String render() {
        StringBuffer sb = new StringBuffer();
        AsciiTable table = createAsciiTable();
        CWC_LongestLine cwc = new CWC_LongestLine();
        cwc.add(20,40).add(60,120);
        table.getRenderer().setCWC(cwc);
        table.addRule();
        table.addRow("Key", "Value");
        table.addRule();
        List<String> columnNames = parseColumnNames(map);
        if(columnNames.isEmpty()){
            table.addRow(null, "(empty)");
            table.addRule();
        }else {
            for (String columnName : columnNames) {
                Object columnValue = getColumnValue(map, columnName);
                table.addRow(columnName, columnValue);
                table.addRule();
            }
        }
        return table.render();
    }

}
