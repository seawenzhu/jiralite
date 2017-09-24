package com.seawen.jiralite.repository;

import com.seawen.jiralite.domain.Code;
import com.seawen.jiralite.domain.CodeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Code entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CodeRepository extends JpaRepository<Code, Long> {

    Page<Code> findByCodeTypeTypeCodeAndCodeLike(String typeCode, String code, Pageable pageable);
    Page<Code> findByCodeTypeTypeCode(String typeCode, Pageable pageable);
    Page<Code> findByCodeLike(String code, Pageable pageable);
}
