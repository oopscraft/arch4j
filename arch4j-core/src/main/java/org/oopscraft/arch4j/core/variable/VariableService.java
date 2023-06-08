package org.oopscraft.arch4j.core.variable;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.variable.repository.VariableEntity;
import org.oopscraft.arch4j.core.variable.repository.VariableRepository;
import org.oopscraft.arch4j.core.variable.repository.VariableSpecification;
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
        VariableEntity variableEntity = variableRepository.findById(variable.getId()).orElse(null);
        if(variableEntity == null) {
            variableEntity = VariableEntity.builder()
                    .id(variable.getId())
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
     * @param id variable id
     * @return variable
     */
    public Optional<Variable> getVariable(String id) {
        return variableRepository.findById(id).map(Variable::from);
    }

    /**
     * delete property
     * @param id property id
     */
    public void deleteVariable(String id) {
        variableRepository.deleteById(id);
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
        if(variableSearch.getId() != null) {
            specification = specification.and(VariableSpecification.likeId(variableSearch.getId()));
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

}
