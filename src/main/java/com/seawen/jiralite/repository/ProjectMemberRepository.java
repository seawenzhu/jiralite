package com.seawen.jiralite.repository;

import com.seawen.jiralite.domain.ProjectMember;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ProjectMember entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {

}
