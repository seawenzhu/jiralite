package com.seawen.jiralite.repository;

import com.seawen.jiralite.domain.Code;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Code entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CodeRepository extends JpaRepository<Code, Long> {

}
