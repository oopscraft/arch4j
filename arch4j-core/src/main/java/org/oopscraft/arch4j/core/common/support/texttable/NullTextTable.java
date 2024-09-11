package org.oopscraft.arch4j.core.common.support.texttable;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestLine;

public class NullTextTable extends TextTable {

    @Override
    String render() {
        StringBuffer sb = new StringBuffer();
        AsciiTable table = createAsciiTable();
        CWC_LongestLine cwc = new CWC_LongestLine();
        table.addRule();
        table.addRow("(null)");
        table.addRule();
        return table.render();
    }

}
