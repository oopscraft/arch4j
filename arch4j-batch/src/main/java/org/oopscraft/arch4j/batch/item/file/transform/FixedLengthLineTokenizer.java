package org.oopscraft.arch4j.batch.item.file.transform;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.jetbrains.annotations.NotNull;
import org.oopscraft.arch4j.batch.item.file.annotation.Length;
import org.springframework.batch.item.file.transform.AbstractLineTokenizer;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.core.convert.TypeDescriptor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
public class FixedLengthLineTokenizer extends AbstractLineTokenizer {

    private final ItemTypeDescriptor itemTypeDescriptor;

    private final List<Range> ranges = new ArrayList<>();

    @Setter
    private String encoding = "UTF-8";

    public FixedLengthLineTokenizer(ItemTypeDescriptor itemTypeDescriptor) {
        this.itemTypeDescriptor = itemTypeDescriptor;

        // set names
        setNames(itemTypeDescriptor.getFieldNames().toArray(new String[0]));

        // defines ranges
        int position = 0;
        for(int i = 0; i < itemTypeDescriptor.getFields().size(); i++){
            TypeDescriptor typeDescriptor = itemTypeDescriptor.getFieldType(i);
            Length lengthAnnotation = typeDescriptor.getAnnotation(Length.class);
            Objects.requireNonNull(lengthAnnotation);
            int length = lengthAnnotation.value();
            int start = position + 1;
            int end = start + length -1;
            ranges.add(new Range(start, end));
            position = end;
        }
    }

    @NotNull
    @Override
    protected List<String> doTokenize(@NotNull String line) {
        try {
            log.debug("[LINE-CHAR][{}]", line);
            List<String> tokens = new ArrayList<>();
            byte[] lineBytes = line.getBytes(encoding);
            log.debug("[LINE-HEX][{}]", String.valueOf(Hex.encodeHex(lineBytes)));
            int i = -1;
            for (Range range : ranges) {
                i ++;
                byte[] tokenBytes = Arrays.copyOfRange(lineBytes, range.getMin()-1, range.getMax());
                String token = new String(tokenBytes, encoding).trim();
                log.debug("[{}][{}][{}]", i, String.valueOf(Hex.encodeHex(tokenBytes)), token);
                tokens.add(token);
            }
            log.debug("[FIELD]{}\n", (Object)tokens.toArray());
            return tokens;
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
