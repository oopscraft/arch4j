package org.chomookun.arch4j.core.variable;

import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.variable.dao.VariableEntity;
import org.chomookun.arch4j.core.variable.dao.VariableRepository;
import org.chomookun.arch4j.core.variable.model.Variable;
import org.chomookun.arch4j.core.variable.model.VariableSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VariableService {

    private final VariableRepository variableRepository;

    @Transactional
    public Variable saveVariable(Variable variable) {
        VariableEntity variableEntity = variableRepository.findById(variable.getVariableId())
                .orElse(VariableEntity.builder()
                    .variableId(variable.getVariableId())
                    .build());

        variableEntity.setSystemUpdatedAt(LocalDateTime.now()); // disable dirty checking
        variableEntity.setValue(variable.getValue());
        variableEntity.setName(variable.getName());
        variableEntity.setNote(variable.getNote());

        VariableEntity savedVariableEntity = variableRepository.saveAndFlush(variableEntity);
        return Variable.from(savedVariableEntity);
    }

    public Optional<Variable> getVariable(String variableId) {
        return variableRepository.findById(variableId).map(Variable::from);
    }

    @Transactional
    public void deleteVariable(String variableId) {
        variableRepository.deleteById(variableId);
        variableRepository.flush();
    }

    public Page<Variable> getVariables(VariableSearch variableSearch, Pageable pageable) {
        Page<VariableEntity> variableEntityPage = variableRepository.findAll(variableSearch, pageable);
        List<Variable> properties = variableEntityPage.getContent().stream()
                .map(Variable::from)
                .collect(Collectors.toList());
        return new PageImpl<>(properties, pageable, variableEntityPage.getTotalElements());
    }

}
