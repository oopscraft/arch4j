package org.chomookun.arch4j.core.message.dao;

import org.chomookun.arch4j.core.message.model.MessageSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, String>, JpaSpecificationExecutor<MessageEntity> {

    default Page<MessageEntity> findAll(MessageSearch messageSearch, Pageable pageable) {
        Specification<MessageEntity> specification = Specification.where(null);

        if(messageSearch.getMessageId() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(MessageEntity_.MESSAGE_ID), '%' + messageSearch.getMessageId() + '%'));
        }

        if(messageSearch.getName() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(MessageEntity_.NAME), '%' + messageSearch.getName() + '%'));
        }

        return findAll(specification, pageable);
    }

}
