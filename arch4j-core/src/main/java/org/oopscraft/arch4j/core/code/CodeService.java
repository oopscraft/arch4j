package org.oopscraft.arch4j.core.code;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.code.dao.CodeEntity;
import org.oopscraft.arch4j.core.code.dao.CodeItemEntity;
import org.oopscraft.arch4j.core.code.dao.CodeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CodeService {

    private final CodeRepository codeRepository;

    @Transactional
    public Code saveCode(Code code) {
        CodeEntity codeEntity = codeRepository.findById(code.getCodeId())
                .orElse(CodeEntity.builder()
                    .codeId(code.getCodeId())
                    .build());

        codeEntity.setCodeName(code.getCodeName());
        codeEntity.setNote(code.getNote());

        List<CodeItemEntity> items = codeEntity.getItems();
        items.clear();
        AtomicInteger sort = new AtomicInteger();
        code.getItems().forEach(item -> items.add(CodeItemEntity.builder()
                .codeId(code.getCodeId())
                .itemId(item.getItemId())
                .sort(sort.getAndIncrement())
                .itemName(item.getItemName())
                .value(item.getValue())
                .build()));

        codeEntity = codeRepository.saveAndFlush(codeEntity);

        return Code.from(codeEntity);
    }

    public Page<Code> getCodes(CodeSearch codeSearch, Pageable pageable) {
        Page<CodeEntity> codeEntityPage = codeRepository.findAll(codeSearch, pageable);
        List<Code> codes = codeEntityPage.getContent().stream()
                .map(Code::from)
                .collect(Collectors.toList());
        return new PageImpl<>(codes, pageable, codeEntityPage.getTotalElements());
    }

    public Optional<Code> getCode(String codeId) {
        return codeRepository.findById(codeId).map(Code::from);
    }

    @Transactional
    public void deleteCode(String codeId) {
        codeRepository.deleteById(codeId);
        codeRepository.flush();
    }

}
