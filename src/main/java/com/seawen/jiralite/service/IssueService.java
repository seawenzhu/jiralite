package com.seawen.jiralite.service;

import com.seawen.jiralite.domain.Issue;
import com.seawen.jiralite.repository.IssueRepository;
import com.seawen.jiralite.repository.search.IssueSearchRepository;
import com.seawen.jiralite.service.dto.IssueDTO;
import com.seawen.jiralite.service.mapper.IssueMapper;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Issue.
 */
@Service
@Transactional
public class IssueService {

    private final Logger log = LoggerFactory.getLogger(IssueService.class);

    private final IssueRepository issueRepository;

    private final IssueMapper issueMapper;

    private final IssueSearchRepository issueSearchRepository;

    public IssueService(IssueRepository issueRepository, IssueMapper issueMapper, IssueSearchRepository issueSearchRepository) {
        this.issueRepository = issueRepository;
        this.issueMapper = issueMapper;
        this.issueSearchRepository = issueSearchRepository;
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
        IssueDTO result = issueMapper.toDto(issue);
        issueSearchRepository.save(issue);
        return result;
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
        issueSearchRepository.delete(id);
    }

    /**
     * Search for the issue corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<IssueDTO> search(String query,
                                 String typeCode,
                                 String statusCode,
                                 String priorityCode, Pageable pageable) {
        log.debug("Request to search for a page of Issues for query {}", query);
        final BoolQueryBuilder qb = new BoolQueryBuilder();
        if (!StringUtils.isEmpty(typeCode)) {
            qb.must(termQuery("issueType", typeCode));
        }
        if (!StringUtils.isEmpty(statusCode)) {
            qb.must(termQuery("issueStatus", statusCode));
        }
        if (!StringUtils.isEmpty(priorityCode)) {
            qb.must(termQuery("issuePriority", priorityCode));
        }
        if (!StringUtils.isEmpty(query)) {
            qb.must(queryStringQuery(query));
        }

        Page<Issue> result = issueSearchRepository.search(qb, pageable);
        return result.map(issueMapper::toDto);
    }
}
