package org.oopscraft.arch4j.core.sample;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.oopscraft.arch4j.core.sample.entity.SampleEntity;
import org.oopscraft.arch4j.core.sample.repository.SampleRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SampleService {

    private final SampleRepository sampleRepository;

    private final ModelMapper modelMapper;

    public void saveSample(Sample sample) {
        SampleEntity sampleEntity = sampleRepository.findById(sample.getId()).orElse(null);
        if(sampleEntity == null) {
            sampleEntity = SampleEntity.builder()
                    .id(sample.getId())
                    .build();
        }
        modelMapper.map(sample, sampleEntity);
        sampleRepository.saveAndFlush(sampleEntity);
    }


}
