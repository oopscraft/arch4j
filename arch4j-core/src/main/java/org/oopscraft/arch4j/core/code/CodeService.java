package org.oopscraft.arch4j.core.code;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.code.repository.CodeEntity;
import org.oopscraft.arch4j.core.code.repository.CodeItemEntity;
import org.oopscraft.arch4j.core.code.repository.CodeRepository;
import org.oopscraft.arch4j.core.code.repository.CodeSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CodeService {

    private final CodeRepository codeRepository;

    /**
     * saves code
     * @param code code info
     * @return code
     */
    public Code saveCode(Code code) {
        CodeEntity codeEntity = codeRepository.findById(code.getCodeId()).orElse(null);
        if(codeEntity == null) {
            codeEntity = CodeEntity.builder()
                    .codeId(code.getCodeId())
                    .build();
        }
        codeEntity.setName(code.getName());
        codeEntity.setNote(code.getNote());

        // replace items
        List<CodeItemEntity> items = codeEntity.getItems();
        items.clear();
        AtomicInteger sort = new AtomicInteger();
        code.getItems().forEach(item -> items.add(CodeItemEntity.builder()
                .codeId(code.getCodeId())
                .itemId(item.getItemId())
                .sort(sort.getAndIncrement())
                .name(item.getName())
                .value(item.getValue())
                .build()));

        // save and flush
        codeEntity = codeRepository.saveAndFlush(codeEntity);
        return Code.from(codeEntity);
    }

    /**
     * returns codes
     * @param codeSearch code search condition
     * @param pageable pagination info
     * @return codes
     */
    public Page<Code> getCodes(CodeSearch codeSearch, Pageable pageable) {

        Specification<CodeEntity> specification = (root, query, criteriaBuilder) -> null;
        if(codeSearch.getCodeId() != null) {
            specification = specification.and(CodeSpecification.likeCodeId(codeSearch.getCodeId()));
        }
        if(codeSearch.getName() != null) {
            specification = specification.and(CodeSpecification.likeName(codeSearch.getName()));
        }

        Page<CodeEntity> codeEntityPage = codeRepository.findAll(specification, pageable);
        List<Code> codes = codeEntityPage.getContent().stream()
                .map(Code::from)
                .collect(Collectors.toList());
        long total = codeEntityPage.getTotalElements();
        return new PageImpl<>(codes, pageable, total);
    }

    /**
     * returns code
     * @param codeId code id
     * @return code info
     */
    public Optional<Code> getCode(String codeId) {
        return codeRepository.findById(codeId).map(Code::from);
    }

    /**
     * deletes code
     * @param codeId code id
     */
    public void deleteCode(String codeId) {
        codeRepository.deleteById(codeId);
        codeRepository.flush();
    }

}
