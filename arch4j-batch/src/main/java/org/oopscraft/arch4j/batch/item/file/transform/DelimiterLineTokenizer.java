package org.oopscraft.arch4j.batch.item.file.transform;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class DelimiterLineTokenizer extends org.springframework.batch.item.file.transform.DelimitedLineTokenizer {

    public DelimiterLineTokenizer(ItemTypeDescriptor itemTypeDescriptor) {
        // column names
        setNames(itemTypeDescriptor.getFieldNames().toArray(new String[0]));
    }

    @NotNull
    @Override
    protected List<String> doTokenize(@NotNull String line) {
        log.debug("[LINE-CHAR][{}]", line);
        List<String> tokens = super.doTokenize(line);

        // converts charset
        List<String> values = new ArrayList<>();
        int i = -1;
        for(String token : tokens) {
            i ++;
            log.debug("[{}][{}]", i, token);
            values.add(token);
        }

        // returns
        log.debug("[FIELD]{}\n", (Object)values.toArray(new String[0]));
        return values;
    }

}
