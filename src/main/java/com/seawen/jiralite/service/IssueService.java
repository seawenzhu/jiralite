package com.seawen.jiralite.service;

import com.seawen.jiralite.domain.Issue;
import com.seawen.jiralite.repository.IssueRepository;
import com.seawen.jiralite.service.dto.IssueDTO;
import com.seawen.jiralite.service.mapper.IssueMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Issue.
 */
@Service
@Transactional
public class IssueService {

    private final Logger log = LoggerFactory.getLogger(IssueService.class);

    private final IssueRepository issueRepository;

    private final IssueMapper issueMapper;

    public IssueService(IssueRepository issueRepository, IssueMapper issueMapper) {
        this.issueRepository = issueRepository;
        this.issueMapper = issueMapper;
    }

    /**
     * Save a issue.
     *
     * @param issueDTO the entity to save
     * @return the persisted entity
     */
    public IssueDTO save(IssueDTO issueDTO) {
        log.debug("Request to save Issue : {}", issueDTO);
        Issue issue = issueMapper.toEntity(issueDTO);
        issue = issueRepository.save(issue);
        return issueMapper.toDto(issue);
    }

    /**
     *  Get all the issues.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<IssueDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Issues");
        return issueRepository.findAll(pageable)
            .map(issueMapper::toDto);
    }

    /**
     *  Get one issue by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public IssueDTO findOne(Long id) {
        log.debug("Request to get Issue : {}", id);
        Issue issue = issueRepository.findOne(id);
        return issueMapper.toDto(issue);
    }

    /**
     *  Delete the  issue by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Issue : {}", id);
        issueRepository.delete(id);
    }
}
