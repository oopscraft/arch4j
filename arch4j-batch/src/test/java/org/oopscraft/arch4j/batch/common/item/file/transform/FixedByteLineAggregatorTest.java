package org.oopscraft.arch4j.batch.common.item.file.transform;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.oopscraft.arch4j.batch.common.item.file.annotation.Length;

@Slf4j
class FixedByteLineAggregatorTest {

    @Data
    public static class TestDto {
        @Length(5)
        private String name;
    }

//    @Test
//    void doAggregate() {
//        // given
//        FieldConversionService conversionService = new FieldConversionService();
//        ItemTypeDescriptor itemTypeDescriptor = new ItemTypeDescriptor(TestDto.class);
//        Faker faker = new Faker(new Locale("ko"));
//        TestDto testDto = new TestDto();
//        testDto.setName(faker.name().fullName());
//        log.info("[{}][{}]", testDto.getName(), testDto.getName().getBytes().length);
//
//        // when
//        FixedByteLineAggregator<Object> lineAggregator = new FixedByteLineAggregator<>("euc-kr", itemTypeDescriptor);
//        lineAggregator.setConversionService(conversionService);
//        String line = lineAggregator.doAggregate(new String[]{testDto.getName()});
//
//        // then
//        log.info("[{}]", line);
//    }

}