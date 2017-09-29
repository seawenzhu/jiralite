package com.seawen.jiralite.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.seawen.jiralite.service.CodeTypeService;
import com.seawen.jiralite.web.rest.util.HeaderUtil;
import com.seawen.jiralite.web.rest.util.PaginationUtil;
import com.seawen.jiralite.service.dto.CodeTypeDTO;
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
 * REST controller for managing CodeType.
 */
@RestController
@RequestMapping("/api")
public class CodeTypeResource {

    private final Logger log = LoggerFactory.getLogger(CodeTypeResource.class);

    private static final String ENTITY_NAME = "codeType";

    private final CodeTypeService codeTypeService;

    public CodeTypeResource(CodeTypeService codeTypeService) {
        this.codeTypeService = codeTypeService;
    }

    /**
     * POST  /code-types : Create a new codeType.
     *
     * @param codeTypeDTO the codeTypeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new codeTypeDTO, or with status 400 (Bad Request) if the codeType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/code-types")
    @Timed
    public ResponseEntity<CodeTypeDTO> createCodeType(@Valid @RequestBody CodeTypeDTO codeTypeDTO) throws URISyntaxException {
        log.debug("REST request to save CodeType : {}", codeTypeDTO);
        if (codeTypeDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new codeType cannot already have an ID")).body(null);
        }
        CodeTypeDTO result = codeTypeService.save(codeTypeDTO);
        return ResponseEntity.created(new URI("/api/code-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /code-types : Updates an existing codeType.
     *
     * @param codeTypeDTO the codeTypeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated codeTypeDTO,
     * or with status 400 (Bad Request) if the codeTypeDTO is not valid,
     * or with status 500 (Internal Server Error) if the codeTypeDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/code-types")
    @Timed
    public ResponseEntity<CodeTypeDTO> updateCodeType(@Valid @RequestBody CodeTypeDTO codeTypeDTO) throws URISyntaxException {
        log.debug("REST request to update CodeType : {}", codeTypeDTO);
        if (codeTypeDTO.getId() == null) {
            return createCodeType(codeTypeDTO);
        }
        CodeTypeDTO result = codeTypeService.save(codeTypeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, codeTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /code-types : get all the codeTypes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of codeTypes in body
     */
    @GetMapping("/code-types")
    @Timed
    public ResponseEntity<List<CodeTypeDTO>> getAllCodeTypes(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of CodeTypes");
        Page<CodeTypeDTO> page = codeTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/code-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /code-types/:id : get the "id" codeType.
     *
     * @param id the id of the codeTypeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the codeTypeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/code-types/{id}")
    @Timed
    public ResponseEntity<CodeTypeDTO> getCodeType(@PathVariable Long id) {
        log.debug("REST request to get CodeType : {}", id);
        CodeTypeDTO codeTypeDTO = codeTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(codeTypeDTO));
    }

    /**
     * DELETE  /code-types/:id : delete the "id" codeType.
     *
     * @param id the id of the codeTypeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/code-types/{id}")
    @Timed
    public ResponseEntity<Void> deleteCodeType(@PathVariable Long id) {
        log.debug("REST request to delete CodeType : {}", id);
        codeTypeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/code-types?query=:query : search for the codeType corresponding
     * to the query.
     *
     * @param query the query of the codeType search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/code-types")
    @Timed
    public ResponseEntity<List<CodeTypeDTO>> searchCodeTypes(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of CodeTypes for query {}", query);
        Page<CodeTypeDTO> page = codeTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/code-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
