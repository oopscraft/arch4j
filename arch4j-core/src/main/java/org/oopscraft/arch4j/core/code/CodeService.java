package org.oopscraft.arch4j.core.code;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.code.repository.CodeEntity;
import org.oopscraft.arch4j.core.code.repository.CodeItemEntity;
import org.oopscraft.arch4j.core.code.repository.CodeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CodeService {

    private final CodeRepository codeRepository;

    public void saveCode(Code code) {
        CodeEntity codeEntity = codeRepository.findById(code.getId()).orElse(null);
        if(codeEntity == null) {
            codeEntity = CodeEntity.builder()
                    .id(code.getId())
                    .build();
        }
        codeEntity.setName(code.getName());
        codeEntity.setNote(code.getNote());
        AtomicInteger index = new AtomicInteger();
        codeEntity.setItems(code.getItems().stream().map(item ->
            CodeItemEntity.builder()
                    .codeId(code.getId())
                    .id(item.getId())
                    .sort(index.getAndIncrement())
                    .name(item.getName())
                    .value(item.getValue())
                    .build()).collect(Collectors.toList()));
        codeRepository.saveAndFlush(codeEntity);
    }


    public Page<Code> getCodes(CodeSearch codeSearch, Pageable pageable) {
        Page<CodeEntity> codeEntityPage = codeRepository.findCodes(codeSearch, pageable);
        List<Code> codes = codeEntityPage.getContent().stream()
                .map(Code::from)
                .collect(Collectors.toList());
        long total = codeEntityPage.getTotalElements();
        return new PageImpl<>(codes, pageable, total);
    }

    public Optional<Code> getCode(String id) {
        return codeRepository.findById(id).map(Code::from);
    }

    public void deleteCode(String id) {
        codeRepository.deleteById(id);
        codeRepository.flush();
    }

}
