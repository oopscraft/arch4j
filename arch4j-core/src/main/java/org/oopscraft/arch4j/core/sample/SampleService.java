package org.oopscraft.arch4j.core.sample;

import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.RowBounds;
import org.modelmapper.ModelMapper;
import org.oopscraft.arch4j.core.sample.dao.SampleEntity;
import org.oopscraft.arch4j.core.sample.dao.SampleMapper;
import org.oopscraft.arch4j.core.sample.dao.SampleRepository;
import org.oopscraft.arch4j.core.sample.dao.SampleSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SampleService {

    private final SampleRepository sampleRepository;

    private final SampleMapper sampleMapper;

    private final ModelMapper modelMapper;

    public Sample saveSample(Sample sample) {
        SampleEntity sampleEntity = sampleRepository.findById(sample.getSampleId()).orElse(null);
        if(sampleEntity == null) {
            sampleEntity = SampleEntity.builder()
                    .sampleId(sample.getSampleId())
                    .build();
        }
        modelMapper.map(sample, sampleEntity);
        sampleEntity = sampleRepository.saveAndFlush(sampleEntity);
        return Sample.from(sampleEntity);
    }

    public Optional<Sample> getSample(String sampleId) {
        return sampleRepository.findById(sampleId).map(sampleEntity -> modelMapper.map(sampleEntity, Sample.class));
    }

    public void deleteSample(String sampleId) {
        sampleRepository.deleteById(sampleId);
        sampleRepository.flush();
    }

    public Page<Sample> getSamplesByJpa(SampleSearch sampleSearch, Pageable pageable) {

        // search condition
        Specification<SampleEntity> specification = (root, query, criteriaBuilder) -> null;
        if(sampleSearch.getSampleId() != null) {
            specification = specification.and(SampleSpecification.likeSampleId(sampleSearch.getSampleId()));
        }
        if(sampleSearch.getSampleName() != null) {
            specification = specification.and(SampleSpecification.likeName(sampleSearch.getSampleName()));
        }
        if(sampleSearch.getSampleType() != null) {
            specification = specification.and(SampleSpecification.equalType(sampleSearch.getSampleType()));
        }

        // find data
        Page<SampleEntity> sampleEntityPage = sampleRepository.findAll(specification, pageable);
        List<Sample> samples  = sampleEntityPage.getContent().stream()
                .map(Sample::from)
                .collect(Collectors.toList());
        long total = sampleEntityPage.getTotalElements();

        // return
        return new PageImpl<>(samples, pageable, total);
    }

    public Page<Sample> getSamplesByQuerydsl(SampleSearch sampleSearch, Pageable pageable) {

        // find
        Page<SampleEntity> sampleVoPage = sampleRepository.findSamples(sampleSearch, pageable);

        // convert
        List<Sample> samples = sampleVoPage.getContent().stream()
                .map(Sample::from)
                .collect(Collectors.toList());
        long total = sampleVoPage.getTotalElements();

        // return
        return new PageImpl<>(samples, pageable, total);
    }

    public Page<Sample> getSamplesByMybatis(SampleSearch sampleSearch, Pageable pageable) {

        // select
        RowBounds rowBounds = new RowBounds((int)pageable.getOffset(), pageable.getPageSize());
        List<Sample> samples = sampleMapper.selectSamples(sampleSearch, rowBounds).stream()
                .map(sampleVo -> modelMapper.map(sampleVo,Sample.class))
                .collect(Collectors.toList());

        // total
        long total = sampleMapper.selectSamplesCount(sampleSearch);

        // return
        return new PageImpl<>(samples, pageable, total);
    }

}
