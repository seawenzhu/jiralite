package com.seawen.jiralite.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.seawen.jiralite.service.CommentsService;
import com.seawen.jiralite.web.rest.util.HeaderUtil;
import com.seawen.jiralite.web.rest.util.PaginationUtil;
import com.seawen.jiralite.service.dto.CommentsDTO;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Comments.
 */
@RestController
@RequestMapping("/api")
public class CommentsResource {

    private final Logger log = LoggerFactory.getLogger(CommentsResource.class);

    private static final String ENTITY_NAME = "comments";

    private final CommentsService commentsService;

    public CommentsResource(CommentsService commentsService) {
        this.commentsService = commentsService;
    }

    /**
     * POST  /comments : Create a new comments.
     *
     * @param commentsDTO the commentsDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new commentsDTO, or with status 400 (Bad Request) if the comments has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/comments")
    @Timed
    public ResponseEntity<CommentsDTO> createComments(@Valid @RequestBody CommentsDTO commentsDTO) throws URISyntaxException {
        log.debug("REST request to save Comments : {}", commentsDTO);
        if (commentsDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new comments cannot already have an ID")).body(null);
        }
        CommentsDTO result = commentsService.save(commentsDTO);
        return ResponseEntity.created(new URI("/api/comments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /comments : Updates an existing comments.
     *
     * @param commentsDTO the commentsDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated commentsDTO,
     * or with status 400 (Bad Request) if the commentsDTO is not valid,
     * or with status 500 (Internal Server Error) if the commentsDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/comments")
    @Timed
    public ResponseEntity<CommentsDTO> updateComments(@Valid @RequestBody CommentsDTO commentsDTO) throws URISyntaxException {
        log.debug("REST request to update Comments : {}", commentsDTO);
        if (commentsDTO.getId() == null) {
            return createComments(commentsDTO);
        }
        CommentsDTO result = commentsService.save(commentsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, commentsDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /comments : get all the comments.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of comments in body
     */
    @GetMapping("/comments")
    @Timed
    public ResponseEntity<List<CommentsDTO>> getAllComments(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Comments");
        Page<CommentsDTO> page = commentsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/comments");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /comments/:id : get the "id" comments.
     *
     * @param id the id of the commentsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the commentsDTO, or with status 404 (Not Found)
     */
    @GetMapping("/comments/{id}")
    @Timed
    public ResponseEntity<CommentsDTO> getComments(@PathVariable Long id) {
        log.debug("REST request to get Comments : {}", id);
        CommentsDTO commentsDTO = commentsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(commentsDTO));
    }

    /**
     * DELETE  /comments/:id : delete the "id" comments.
     *
     * @param id the id of the commentsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/comments/{id}")
    @Timed
    public ResponseEntity<Void> deleteComments(@PathVariable Long id) {
        log.debug("REST request to delete Comments : {}", id);
        commentsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/comments?query=:query : search for the comments corresponding
     * to the query.
     *
     * @param query the query of the comments search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/comments")
    @Timed
    public ResponseEntity<List<CommentsDTO>> searchComments(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Comments for query {}", query);
        Page<CommentsDTO> page = commentsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/comments");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
