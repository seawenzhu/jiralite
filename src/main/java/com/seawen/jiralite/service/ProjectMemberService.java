package com.seawen.jiralite.service;

import com.seawen.jiralite.domain.ProjectMember;
import com.seawen.jiralite.repository.ProjectMemberRepository;
import com.seawen.jiralite.repository.search.ProjectMemberSearchRepository;
import com.seawen.jiralite.service.dto.ProjectMemberDTO;
import com.seawen.jiralite.service.mapper.ProjectMemberMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing ProjectMember.
 */
@Service
@Transactional
public class ProjectMemberService {

    private final Logger log = LoggerFactory.getLogger(ProjectMemberService.class);

    private final ProjectMemberRepository projectMemberRepository;

    private final ProjectMemberMapper projectMemberMapper;

    private final ProjectMemberSearchRepository projectMemberSearchRepository;

    public ProjectMemberService(ProjectMemberRepository projectMemberRepository, ProjectMemberMapper projectMemberMapper, ProjectMemberSearchRepository projectMemberSearchRepository) {
        this.projectMemberRepository = projectMemberRepository;
        this.projectMemberMapper = projectMemberMapper;
        this.projectMemberSearchRepository = projectMemberSearchRepository;
    }

    /**
     * Save a projectMember.
     *
     * @param projectMemberDTO the entity to save
     * @return the persisted entity
     */
    public ProjectMemberDTO save(ProjectMemberDTO projectMemberDTO) {
        log.debug("Request to save ProjectMember : {}", projectMemberDTO);
        ProjectMember projectMember = projectMemberMapper.toEntity(projectMemberDTO);
        projectMember = projectMemberRepository.save(projectMember);
        ProjectMemberDTO result = projectMemberMapper.toDto(projectMember);
        projectMemberSearchRepository.save(projectMember);
        return result;
    }

    /**
     *  Get all the projectMembers.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ProjectMemberDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProjectMembers");
        return projectMemberRepository.findAll(pageable)
            .map(projectMemberMapper::toDto);
    }

    /**
     *  Get one projectMember by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public ProjectMemberDTO findOne(Long id) {
        log.debug("Request to get ProjectMember : {}", id);
        ProjectMember projectMember = projectMemberRepository.findOne(id);
        return projectMemberMapper.toDto(projectMember);
    }

    /**
     *  Delete the  projectMember by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ProjectMember : {}", id);
        projectMemberRepository.delete(id);
        projectMemberSearchRepository.delete(id);
    }

    /**
     * Search for the projectMember corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ProjectMemberDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ProjectMembers for query {}", query);
        Page<ProjectMember> result = projectMemberSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(projectMemberMapper::toDto);
    }
}
