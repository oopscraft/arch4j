package org.chomoo.arch4j.core.common.support.texttable;

import de.vandermeer.asciitable.AsciiTable;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class CollectionTextTable extends TextTable {

    private final Collection collection;

    @Override
    String render() {
        AsciiTable table = createAsciiTable();

        // if empty row
        if(collection.isEmpty()){
            table.addRule();
            table.addRow("(empty)");
            table.addRule();
            return table.render();
        }

        // header
        Object firstOne = collection.iterator().next();
        List<String> columnNames = parseColumnNames(firstOne);
        table.addRule();
        table.addRow(columnNames.toArray(new Object[columnNames.size()]));
        table.addRule();

        // records
        for(Object record : collection) {
            List<String> columnValues = new ArrayList<>();
            for(String columnName : columnNames) {
                String columnValue = getColumnValue(record, columnName);
                columnValues.add(columnValue);
            }
            table.addRow(columnValues.toArray(new Object[columnValues.size()]));
            table.addRule();
        }

        // returns
        return table.render();
    }
}
