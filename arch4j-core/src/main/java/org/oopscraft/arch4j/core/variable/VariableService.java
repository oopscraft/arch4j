package org.oopscraft.arch4j.core.variable;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.variable.dao.VariableEntity;
import org.oopscraft.arch4j.core.variable.dao.VariableRepository;
import org.oopscraft.arch4j.core.variable.dao.VariableSpecification;
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
public class VariableService {

    private final VariableRepository variableRepository;

    /**
     * saveVariable
     * @param variable variable
     */
    public Variable saveVariable(Variable variable) {
        VariableEntity variableEntity = variableRepository.findById(variable.getVariableId()).orElse(null);
        if(variableEntity == null) {
            variableEntity = VariableEntity.builder()
                    .variableId(variable.getVariableId())
                    .build();
        }
        variableEntity.setValue(variable.getValue());
        variableEntity.setName(variable.getName());
        variableEntity.setNote(variable.getNote());
        variableEntity = variableRepository.saveAndFlush(variableEntity);
        return Variable.from(variableEntity);
    }

    /**
     * get variable
     * @param variableId variable id
     * @return variable
     */
    public Optional<Variable> getVariable(String variableId) {
        return variableRepository.findById(variableId).map(Variable::from);
    }

    /**
     * delete property
     * @param variableId property id
     */
    public void deleteVariable(String variableId) {
        variableRepository.deleteById(variableId);
        variableRepository.flush();
    }

    /**
     * getVariables
     * @param variableSearch variable search condition
     * @param pageable pageable
     * @return property page
     */
    public Page<Variable> getVariables(VariableSearch variableSearch, Pageable pageable) {

        // search condition
        Specification<VariableEntity> specification = (root, query, criteriaBuilder) -> null;
        if(variableSearch.getVariableId() != null) {
            specification = specification.and(VariableSpecification.likeVariableId(variableSearch.getVariableId()));
        }
        if(variableSearch.getName() != null) {
            specification = specification.and(VariableSpecification.likeName(variableSearch.getName()));
        }

        // find data
        Page<VariableEntity> variableEntityPage = variableRepository.findAll(specification, pageable);
        List<Variable> properties = variableEntityPage.getContent().stream()
                .map(Variable::from)
                .collect(Collectors.toList());
        long total = variableEntityPage.getTotalElements();

        // return
        return new PageImpl<>(properties, pageable, total);
    }

    /**
     * return variable value
     * @param variableId variable id
     * @return variable value
     */
    public String getVariableValue(String variableId) {
        return getVariable(variableId)
                .map(Variable::getValue)
                .orElse(null);
    }

}
