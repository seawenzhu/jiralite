package com.seawen.jiralite.service;

import com.seawen.jiralite.domain.Comments;
import com.seawen.jiralite.repository.CommentsRepository;
import com.seawen.jiralite.service.dto.CommentsDTO;
import com.seawen.jiralite.service.mapper.CommentsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Comments.
 */
@Service
@Transactional
public class CommentsService {

    private final Logger log = LoggerFactory.getLogger(CommentsService.class);

    private final CommentsRepository commentsRepository;

    private final CommentsMapper commentsMapper;

    public CommentsService(CommentsRepository commentsRepository, CommentsMapper commentsMapper) {
        this.commentsRepository = commentsRepository;
        this.commentsMapper = commentsMapper;
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
        comments = commentsRepository.save(comments);
        return commentsMapper.toDto(comments);
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
    }
}
