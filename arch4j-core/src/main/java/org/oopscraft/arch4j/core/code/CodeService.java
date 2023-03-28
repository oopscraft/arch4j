package org.oopscraft.arch4j.core.code;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CodeService {

    private final CodeRepository codeRepository;

    /**
     * save code
     * @param code
     */
    public void saveCode(Code code) {
        Code one = codeRepository.findById(code.getId()).orElse(null);
        if(one == null) {
            one = Code.builder()
                    .id(code.getId())
                    .build();
        }
        one.setName(code.getName());
        one.setNote(code.getNote());
        one.setItems(code.getItems());
        codeRepository.saveAndFlush(one);
    }

    /**
     * get codes
     * @return code list
     */
    public Page<Code> getCodes(CodeSearch codeSearch, Pageable pageable) {
        return codeRepository.findCodes(codeSearch, pageable);
    }

    /**
     * get code
     * @param id
     * @return
     */
    public Optional<Code> getCode(String id) {
        return codeRepository.findById(id);
    }

    /**
     * delete code
     * @param code
     */
    public void deleteCode(String id) {
        codeRepository.deleteById(id);
        codeRepository.flush();
    }

}
