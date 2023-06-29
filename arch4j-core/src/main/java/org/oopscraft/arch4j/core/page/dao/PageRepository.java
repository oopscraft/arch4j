package org.oopscraft.arch4j.core.page.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PageRepository extends JpaRepository<PageEntity, String>, JpaSpecificationExecutor<PageEntity>  {

}
