package org.oopscraft.arch4j.batch.item.file.transform;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.jetbrains.annotations.NotNull;
import org.oopscraft.arch4j.batch.item.file.annotation.Align;
import org.oopscraft.arch4j.batch.item.file.annotation.Length;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.ExtractorLineAggregator;
import org.springframework.batch.item.file.transform.FieldExtractor;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.util.Assert;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class FixedByteLineAggregator<T> implements LineAggregator<T> {

    protected final String encoding;

    protected final ItemTypeDescriptor itemTypeDescriptor;

    protected final FieldExtractor<T> fieldExtractor;

    protected final ConversionService conversionService;

    public FixedByteLineAggregator(String encoding, ItemTypeDescriptor itemTypeDescriptor, FieldExtractor<T> fieldExtractor, ConversionService conversionService) {
        this.encoding = encoding;
        this.itemTypeDescriptor = itemTypeDescriptor;
        this.fieldExtractor = fieldExtractor;
        this.conversionService = conversionService;
    }

    @NotNull
    @Override
    public String aggregate(@NotNull T item) {
        Assert.notNull(item, "Item is required");
        Object[] fields = this.fieldExtractor.extract(item);

        log.debug("[FIELD]{}", (Object)fields);
        try (ByteArrayOutputStream lineBytes = new ByteArrayOutputStream()) {
            for(int i = 0; i < fields.length; i ++) {
                Object field = fields[i];

                // convert value
                TypeDescriptor sourceType = itemTypeDescriptor.getFieldType(i);
                TypeDescriptor targetType = TypeDescriptor.valueOf(String.class);
                String value = Optional.ofNullable(conversionService.convert(field, sourceType, targetType))
                        .map(Object::toString)
                        .orElse("");

                // length annotation
                Length lengthAnnotation = sourceType.getAnnotation(Length.class);
                Objects.requireNonNull(lengthAnnotation);
                int length = lengthAnnotation.value();
                char padChar = (lengthAnnotation.padChar() == '\0' ? ' ' : lengthAnnotation.padChar());

                // convert field bytes
                byte[] sourceBytes = value.getBytes();
                byte[] targetBytes = null;
                if(lengthAnnotation.align() == Align.RIGHT) {
                    targetBytes = alignRight(sourceBytes, length, (byte)padChar);
                }else{
                    targetBytes = alignLeft(sourceBytes, length, (byte)padChar);
                }

                // write to buffer
                log.debug("[{}][{}][{}][{}]", i, fields[i], new String(targetBytes), String.valueOf(Hex.encodeHex(targetBytes)));
                lineBytes.write(targetBytes);
            }

            // returns
            log.debug("[LINE-HEX][{}]", String.valueOf(Hex.encodeHex(lineBytes.toByteArray())));
            String line = lineBytes.toString();
            log.debug("[LINE-CHAR][{}]\n", line);
            return line;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] alignLeft(byte[] sourceBytes, int length, byte padByte) {
        byte[] targetBytes = new byte[length];
        Arrays.fill(targetBytes, padByte);
        System.arraycopy(
                sourceBytes,
                0,
                targetBytes,
                0,
                Math.min(sourceBytes.length, length)
        );
        return targetBytes;
    }

    public byte[] alignRight(byte[] sourceBytes, int length, byte padByte) {
        byte[] targetBytes = new byte[length];
        Arrays.fill(targetBytes, padByte);
        System.arraycopy(
                sourceBytes,
                0,
                targetBytes,
                Math.max(length - sourceBytes.length,0),
                Math.min(sourceBytes.length, length)
        );
        return targetBytes;
    }

}
