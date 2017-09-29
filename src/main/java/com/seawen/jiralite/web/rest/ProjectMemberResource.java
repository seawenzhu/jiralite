package com.seawen.jiralite.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.seawen.jiralite.service.ProjectMemberService;
import com.seawen.jiralite.web.rest.util.HeaderUtil;
import com.seawen.jiralite.web.rest.util.PaginationUtil;
import com.seawen.jiralite.service.dto.ProjectMemberDTO;
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
 * REST controller for managing ProjectMember.
 */
@RestController
@RequestMapping("/api")
public class ProjectMemberResource {

    private final Logger log = LoggerFactory.getLogger(ProjectMemberResource.class);

    private static final String ENTITY_NAME = "projectMember";

    private final ProjectMemberService projectMemberService;

    public ProjectMemberResource(ProjectMemberService projectMemberService) {
        this.projectMemberService = projectMemberService;
    }

    /**
     * POST  /project-members : Create a new projectMember.
     *
     * @param projectMemberDTO the projectMemberDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new projectMemberDTO, or with status 400 (Bad Request) if the projectMember has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/project-members")
    @Timed
    public ResponseEntity<ProjectMemberDTO> createProjectMember(@Valid @RequestBody ProjectMemberDTO projectMemberDTO) throws URISyntaxException {
        log.debug("REST request to save ProjectMember : {}", projectMemberDTO);
        if (projectMemberDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new projectMember cannot already have an ID")).body(null);
        }
        ProjectMemberDTO result = projectMemberService.save(projectMemberDTO);
        return ResponseEntity.created(new URI("/api/project-members/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /project-members : Updates an existing projectMember.
     *
     * @param projectMemberDTO the projectMemberDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated projectMemberDTO,
     * or with status 400 (Bad Request) if the projectMemberDTO is not valid,
     * or with status 500 (Internal Server Error) if the projectMemberDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/project-members")
    @Timed
    public ResponseEntity<ProjectMemberDTO> updateProjectMember(@Valid @RequestBody ProjectMemberDTO projectMemberDTO) throws URISyntaxException {
        log.debug("REST request to update ProjectMember : {}", projectMemberDTO);
        if (projectMemberDTO.getId() == null) {
            return createProjectMember(projectMemberDTO);
        }
        ProjectMemberDTO result = projectMemberService.save(projectMemberDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, projectMemberDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /project-members : get all the projectMembers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of projectMembers in body
     */
    @GetMapping("/project-members")
    @Timed
    public ResponseEntity<List<ProjectMemberDTO>> getAllProjectMembers(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of ProjectMembers");
        Page<ProjectMemberDTO> page = projectMemberService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/project-members");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /project-members/:id : get the "id" projectMember.
     *
     * @param id the id of the projectMemberDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the projectMemberDTO, or with status 404 (Not Found)
     */
    @GetMapping("/project-members/{id}")
    @Timed
    public ResponseEntity<ProjectMemberDTO> getProjectMember(@PathVariable Long id) {
        log.debug("REST request to get ProjectMember : {}", id);
        ProjectMemberDTO projectMemberDTO = projectMemberService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(projectMemberDTO));
    }

    /**
     * DELETE  /project-members/:id : delete the "id" projectMember.
     *
     * @param id the id of the projectMemberDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/project-members/{id}")
    @Timed
    public ResponseEntity<Void> deleteProjectMember(@PathVariable Long id) {
        log.debug("REST request to delete ProjectMember : {}", id);
        projectMemberService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/project-members?query=:query : search for the projectMember corresponding
     * to the query.
     *
     * @param query the query of the projectMember search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/project-members")
    @Timed
    public ResponseEntity<List<ProjectMemberDTO>> searchProjectMembers(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of ProjectMembers for query {}", query);
        Page<ProjectMemberDTO> page = projectMemberService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/project-members");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
