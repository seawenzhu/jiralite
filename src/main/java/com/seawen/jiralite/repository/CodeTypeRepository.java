package com.seawen.jiralite.repository;

import com.seawen.jiralite.domain.CodeType;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the CodeType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CodeTypeRepository extends JpaRepository<CodeType, Long> {

}
