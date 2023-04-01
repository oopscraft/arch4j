package org.oopscraft.arch4j.core.sample;

import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.RowBounds;
import org.modelmapper.ModelMapper;
import org.oopscraft.arch4j.core.sample.entity.SampleEntity;
import org.oopscraft.arch4j.core.sample.mapper.SampleMapper;
import org.oopscraft.arch4j.core.sample.repository.SampleRepository;
import org.oopscraft.arch4j.core.sample.repository.SampleSpecification;
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

    /**
     * save sample
     * @param sample sample
     */
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

    /**
     * return sample
     * @param id sample id
     * @return sample
     */
    public Optional<Sample> getSample(String id) {
        return sampleRepository.findById(id).map(sampleEntity -> modelMapper.map(sampleEntity, Sample.class));
    }

    /**
     * delete sample
     * @param id sample id
     */
    public void deleteSample(String id) {
        sampleRepository.deleteById(id);
        sampleRepository.flush();
    }

    /**
     * search samples by jpa
     * @param sampleSearch search condition
     * @param pageable pageable
     * @return sample page
     */
    public Page<Sample> getSamplesByJpa(SampleSearch sampleSearch, Pageable pageable) {

        // search condition
        Specification<SampleEntity> specification = (root, query, criteriaBuilder) -> null;
        if(sampleSearch.getId() != null) {
            specification = specification.and(SampleSpecification.likeId(sampleSearch.getId()));
        }
        if(sampleSearch.getName() != null) {
            specification = specification.and(SampleSpecification.likeName(sampleSearch.getName()));
        }
        if(sampleSearch.getType() != null) {
            specification = specification.and(SampleSpecification.equalType(sampleSearch.getType()));
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

    /**
     * search sample by querydsl
     * @param sampleSearch sample search
     * @param pageable pageable
     * @return sample page
     */
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

    /**
     * search samples by mybatis
     * @param sampleSearch search condition
     * @param pageable pageable
     * @return sample page
     */
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
