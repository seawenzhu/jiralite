package com.seawen.jiralite.service;

import com.seawen.jiralite.domain.Comments;
import com.seawen.jiralite.domain.Issue;
import com.seawen.jiralite.repository.CommentsRepository;
import com.seawen.jiralite.repository.IssueRepository;
import com.seawen.jiralite.repository.search.CommentsSearchRepository;
import com.seawen.jiralite.service.dto.CommentsDTO;
import com.seawen.jiralite.service.mapper.CommentsMapper;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Comments.
 */
@Service
@Transactional
public class CommentsService {

    private final Logger log = LoggerFactory.getLogger(CommentsService.class);

    private final CommentsRepository commentsRepository;
    private final IssueRepository issueRepository;

    private final CommentsMapper commentsMapper;

    private final CommentsSearchRepository commentsSearchRepository;

    public CommentsService(CommentsRepository commentsRepository,
                           IssueRepository issueRepository,
                           CommentsMapper commentsMapper,
                           CommentsSearchRepository commentsSearchRepository) {
        this.commentsRepository = commentsRepository;
        this.issueRepository = issueRepository;
        this.commentsMapper = commentsMapper;
        this.commentsSearchRepository = commentsSearchRepository;
    }

    /**
     * Save a comments.
     *
     * @param commentsDTO the entity to save
     * @return the persisted entity
     */
    public CommentsDTO save(CommentsDTO commentsDTO) {
        log.debug("Request to save Comments : {}", commentsDTO);
        Comments comments = commentsMapper.toEntity(commentsDTO);
        Issue issue = this.issueRepository.findOne(comments.getIssue().getId());
        comments.setIssue(issue);
        comments = commentsRepository.save(comments);
        CommentsDTO result = commentsMapper.toDto(comments);
        commentsSearchRepository.save(comments);
        return result;
    }

    /**
     *  Get all the comments.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<CommentsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Comments");
        return commentsRepository.findAll(pageable)
            .map(commentsMapper::toDto);
    }

    /**
     *  Get all the comments.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<CommentsDTO> findAllByIssueId(Long issueId, Pageable pageable) {
        log.debug("Request to get all Comments");
        return commentsRepository.findAllByIssueId(issueId, pageable)
            .map(commentsMapper::toDto);
    }

    /**
     *  Get one comments by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public CommentsDTO findOne(Long id) {
        log.debug("Request to get Comments : {}", id);
        Comments comments = commentsRepository.findOne(id);
        return commentsMapper.toDto(comments);
    }

    /**
     *  Delete the  comments by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Comments : {}", id);
        commentsRepository.delete(id);
        commentsSearchRepository.delete(id);
    }

    /**
     * Search for the comments corresponding to the query.
     *
     *
     * @param issueId
     * @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<CommentsDTO> search(Long issueId, String query, Pageable pageable) {
        log.debug("Request to search for a page of Comments for query {}", query);
        final BoolQueryBuilder qb = new BoolQueryBuilder();
        if (issueId != null) {
            qb.must(termQuery("issueId", issueId));
        }
        qb.must(queryStringQuery(query));
        Page<Comments> result = commentsSearchRepository.search(qb, pageable);
        return result.map(commentsMapper::toDto);
    }
}
