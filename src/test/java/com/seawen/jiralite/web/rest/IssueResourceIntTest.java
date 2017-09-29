package com.seawen.jiralite.web.rest;

import com.seawen.jiralite.JiraliteApp;

import com.seawen.jiralite.domain.Issue;
import com.seawen.jiralite.repository.IssueRepository;
import com.seawen.jiralite.service.IssueService;
import com.seawen.jiralite.repository.search.IssueSearchRepository;
import com.seawen.jiralite.service.dto.IssueDTO;
import com.seawen.jiralite.service.mapper.IssueMapper;
import com.seawen.jiralite.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the IssueResource REST controller.
 *
 * @see IssueResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JiraliteApp.class)
public class IssueResourceIntTest {

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_VERSION = 1;
    private static final Integer UPDATED_VERSION = 2;

    private static final String DEFAULT_ISSUE_NO = "AAAAAAAAAA";
    private static final String UPDATED_ISSUE_NO = "BBBBBBBBBB";

    private static final String DEFAULT_ISSUE_SUBJECT = "AAAAAAAAAA";
    private static final String UPDATED_ISSUE_SUBJECT = "BBBBBBBBBB";

    private static final String DEFAULT_ISSUE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_ISSUE_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_ISSUE_PRIORITY = "AAAAAAAAAA";
    private static final String UPDATED_ISSUE_PRIORITY = "BBBBBBBBBB";

    private static final String DEFAULT_ISSUE_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_ISSUE_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_REPORTER = "AAAAAAAAAA";
    private static final String UPDATED_REPORTER = "BBBBBBBBBB";

    private static final String DEFAULT_ASSIGNER = "AAAAAAAAAA";
    private static final String UPDATED_ASSIGNER = "BBBBBBBBBB";

    private static final String DEFAULT_REMARK = "AAAAAAAAAA";
    private static final String UPDATED_REMARK = "BBBBBBBBBB";

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private IssueMapper issueMapper;

    @Autowired
    private IssueService issueService;

    @Autowired
    private IssueSearchRepository issueSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restIssueMockMvc;

    private Issue issue;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final IssueResource issueResource = new IssueResource(issueService);
        this.restIssueMockMvc = MockMvcBuilders.standaloneSetup(issueResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Issue createEntity(EntityManager em) {
        Issue issue = new Issue()
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .version(DEFAULT_VERSION)
            .issueNo(DEFAULT_ISSUE_NO)
            .issueSubject(DEFAULT_ISSUE_SUBJECT)
            .issueType(DEFAULT_ISSUE_TYPE)
            .issuePriority(DEFAULT_ISSUE_PRIORITY)
            .issueStatus(DEFAULT_ISSUE_STATUS)
            .reporter(DEFAULT_REPORTER)
            .assigner(DEFAULT_ASSIGNER)
            .remark(DEFAULT_REMARK);
        return issue;
    }

    @Before
    public void initTest() {
        issueSearchRepository.deleteAll();
        issue = createEntity(em);
    }

    @Test
    @Transactional
    public void createIssue() throws Exception {
        int databaseSizeBeforeCreate = issueRepository.findAll().size();

        // Create the Issue
        IssueDTO issueDTO = issueMapper.toDto(issue);
        restIssueMockMvc.perform(post("/api/issues")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(issueDTO)))
            .andExpect(status().isCreated());

        // Validate the Issue in the database
        List<Issue> issueList = issueRepository.findAll();
        assertThat(issueList).hasSize(databaseSizeBeforeCreate + 1);
        Issue testIssue = issueList.get(issueList.size() - 1);
        assertThat(testIssue.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testIssue.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testIssue.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testIssue.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testIssue.getVersion()).isEqualTo(DEFAULT_VERSION);
        assertThat(testIssue.getIssueNo()).isEqualTo(DEFAULT_ISSUE_NO);
        assertThat(testIssue.getIssueSubject()).isEqualTo(DEFAULT_ISSUE_SUBJECT);
        assertThat(testIssue.getIssueType()).isEqualTo(DEFAULT_ISSUE_TYPE);
        assertThat(testIssue.getIssuePriority()).isEqualTo(DEFAULT_ISSUE_PRIORITY);
        assertThat(testIssue.getIssueStatus()).isEqualTo(DEFAULT_ISSUE_STATUS);
        assertThat(testIssue.getReporter()).isEqualTo(DEFAULT_REPORTER);
        assertThat(testIssue.getAssigner()).isEqualTo(DEFAULT_ASSIGNER);
        assertThat(testIssue.getRemark()).isEqualTo(DEFAULT_REMARK);

        // Validate the Issue in Elasticsearch
        Issue issueEs = issueSearchRepository.findOne(testIssue.getId());
        assertThat(issueEs).isEqualToComparingFieldByField(testIssue);
    }

    @Test
    @Transactional
    public void createIssueWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = issueRepository.findAll().size();

        // Create the Issue with an existing ID
        issue.setId(1L);
        IssueDTO issueDTO = issueMapper.toDto(issue);

        // An entity with an existing ID cannot be created, so this API call must fail
        restIssueMockMvc.perform(post("/api/issues")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(issueDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Issue in the database
        List<Issue> issueList = issueRepository.findAll();
        assertThat(issueList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkIssueNoIsRequired() throws Exception {
        int databaseSizeBeforeTest = issueRepository.findAll().size();
        // set the field null
        issue.setIssueNo(null);

        // Create the Issue, which fails.
        IssueDTO issueDTO = issueMapper.toDto(issue);

        restIssueMockMvc.perform(post("/api/issues")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(issueDTO)))
            .andExpect(status().isBadRequest());

        List<Issue> issueList = issueRepository.findAll();
        assertThat(issueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIssueTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = issueRepository.findAll().size();
        // set the field null
        issue.setIssueType(null);

        // Create the Issue, which fails.
        IssueDTO issueDTO = issueMapper.toDto(issue);

        restIssueMockMvc.perform(post("/api/issues")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(issueDTO)))
            .andExpect(status().isBadRequest());

        List<Issue> issueList = issueRepository.findAll();
        assertThat(issueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIssuePriorityIsRequired() throws Exception {
        int databaseSizeBeforeTest = issueRepository.findAll().size();
        // set the field null
        issue.setIssuePriority(null);

        // Create the Issue, which fails.
        IssueDTO issueDTO = issueMapper.toDto(issue);

        restIssueMockMvc.perform(post("/api/issues")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(issueDTO)))
            .andExpect(status().isBadRequest());

        List<Issue> issueList = issueRepository.findAll();
        assertThat(issueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRemarkIsRequired() throws Exception {
        int databaseSizeBeforeTest = issueRepository.findAll().size();
        // set the field null
        issue.setRemark(null);

        // Create the Issue, which fails.
        IssueDTO issueDTO = issueMapper.toDto(issue);

        restIssueMockMvc.perform(post("/api/issues")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(issueDTO)))
            .andExpect(status().isBadRequest());

        List<Issue> issueList = issueRepository.findAll();
        assertThat(issueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllIssues() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get all the issueList
        restIssueMockMvc.perform(get("/api/issues?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(issue.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].issueNo").value(hasItem(DEFAULT_ISSUE_NO.toString())))
            .andExpect(jsonPath("$.[*].issueSubject").value(hasItem(DEFAULT_ISSUE_SUBJECT.toString())))
            .andExpect(jsonPath("$.[*].issueType").value(hasItem(DEFAULT_ISSUE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].issuePriority").value(hasItem(DEFAULT_ISSUE_PRIORITY.toString())))
            .andExpect(jsonPath("$.[*].issueStatus").value(hasItem(DEFAULT_ISSUE_STATUS.toString())))
            .andExpect(jsonPath("$.[*].reporter").value(hasItem(DEFAULT_REPORTER.toString())))
            .andExpect(jsonPath("$.[*].assigner").value(hasItem(DEFAULT_ASSIGNER.toString())))
            .andExpect(jsonPath("$.[*].remark").value(hasItem(DEFAULT_REMARK.toString())));
    }

    @Test
    @Transactional
    public void getIssue() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get the issue
        restIssueMockMvc.perform(get("/api/issues/{id}", issue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(issue.getId().intValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION))
            .andExpect(jsonPath("$.issueNo").value(DEFAULT_ISSUE_NO.toString()))
            .andExpect(jsonPath("$.issueSubject").value(DEFAULT_ISSUE_SUBJECT.toString()))
            .andExpect(jsonPath("$.issueType").value(DEFAULT_ISSUE_TYPE.toString()))
            .andExpect(jsonPath("$.issuePriority").value(DEFAULT_ISSUE_PRIORITY.toString()))
            .andExpect(jsonPath("$.issueStatus").value(DEFAULT_ISSUE_STATUS.toString()))
            .andExpect(jsonPath("$.reporter").value(DEFAULT_REPORTER.toString()))
            .andExpect(jsonPath("$.assigner").value(DEFAULT_ASSIGNER.toString()))
            .andExpect(jsonPath("$.remark").value(DEFAULT_REMARK.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingIssue() throws Exception {
        // Get the issue
        restIssueMockMvc.perform(get("/api/issues/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateIssue() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);
        issueSearchRepository.save(issue);
        int databaseSizeBeforeUpdate = issueRepository.findAll().size();

        // Update the issue
        Issue updatedIssue = issueRepository.findOne(issue.getId());
        updatedIssue
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .version(UPDATED_VERSION)
            .issueNo(UPDATED_ISSUE_NO)
            .issueSubject(UPDATED_ISSUE_SUBJECT)
            .issueType(UPDATED_ISSUE_TYPE)
            .issuePriority(UPDATED_ISSUE_PRIORITY)
            .issueStatus(UPDATED_ISSUE_STATUS)
            .reporter(UPDATED_REPORTER)
            .assigner(UPDATED_ASSIGNER)
            .remark(UPDATED_REMARK);
        IssueDTO issueDTO = issueMapper.toDto(updatedIssue);

        restIssueMockMvc.perform(put("/api/issues")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(issueDTO)))
            .andExpect(status().isOk());

        // Validate the Issue in the database
        List<Issue> issueList = issueRepository.findAll();
        assertThat(issueList).hasSize(databaseSizeBeforeUpdate);
        Issue testIssue = issueList.get(issueList.size() - 1);
        assertThat(testIssue.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testIssue.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testIssue.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testIssue.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testIssue.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testIssue.getIssueNo()).isEqualTo(UPDATED_ISSUE_NO);
        assertThat(testIssue.getIssueSubject()).isEqualTo(UPDATED_ISSUE_SUBJECT);
        assertThat(testIssue.getIssueType()).isEqualTo(UPDATED_ISSUE_TYPE);
        assertThat(testIssue.getIssuePriority()).isEqualTo(UPDATED_ISSUE_PRIORITY);
        assertThat(testIssue.getIssueStatus()).isEqualTo(UPDATED_ISSUE_STATUS);
        assertThat(testIssue.getReporter()).isEqualTo(UPDATED_REPORTER);
        assertThat(testIssue.getAssigner()).isEqualTo(UPDATED_ASSIGNER);
        assertThat(testIssue.getRemark()).isEqualTo(UPDATED_REMARK);

        // Validate the Issue in Elasticsearch
        Issue issueEs = issueSearchRepository.findOne(testIssue.getId());
        assertThat(issueEs).isEqualToComparingFieldByField(testIssue);
    }

    @Test
    @Transactional
    public void updateNonExistingIssue() throws Exception {
        int databaseSizeBeforeUpdate = issueRepository.findAll().size();

        // Create the Issue
        IssueDTO issueDTO = issueMapper.toDto(issue);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restIssueMockMvc.perform(put("/api/issues")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(issueDTO)))
            .andExpect(status().isCreated());

        // Validate the Issue in the database
        List<Issue> issueList = issueRepository.findAll();
        assertThat(issueList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteIssue() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);
        issueSearchRepository.save(issue);
        int databaseSizeBeforeDelete = issueRepository.findAll().size();

        // Get the issue
        restIssueMockMvc.perform(delete("/api/issues/{id}", issue.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean issueExistsInEs = issueSearchRepository.exists(issue.getId());
        assertThat(issueExistsInEs).isFalse();

        // Validate the database is empty
        List<Issue> issueList = issueRepository.findAll();
        assertThat(issueList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchIssue() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);
        issueSearchRepository.save(issue);

        // Search the issue
        restIssueMockMvc.perform(get("/api/_search/issues?query=id:" + issue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(issue.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].issueNo").value(hasItem(DEFAULT_ISSUE_NO.toString())))
            .andExpect(jsonPath("$.[*].issueSubject").value(hasItem(DEFAULT_ISSUE_SUBJECT.toString())))
            .andExpect(jsonPath("$.[*].issueType").value(hasItem(DEFAULT_ISSUE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].issuePriority").value(hasItem(DEFAULT_ISSUE_PRIORITY.toString())))
            .andExpect(jsonPath("$.[*].issueStatus").value(hasItem(DEFAULT_ISSUE_STATUS.toString())))
            .andExpect(jsonPath("$.[*].reporter").value(hasItem(DEFAULT_REPORTER.toString())))
            .andExpect(jsonPath("$.[*].assigner").value(hasItem(DEFAULT_ASSIGNER.toString())))
            .andExpect(jsonPath("$.[*].remark").value(hasItem(DEFAULT_REMARK.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Issue.class);
        Issue issue1 = new Issue();
        issue1.setId(1L);
        Issue issue2 = new Issue();
        issue2.setId(issue1.getId());
        assertThat(issue1).isEqualTo(issue2);
        issue2.setId(2L);
        assertThat(issue1).isNotEqualTo(issue2);
        issue1.setId(null);
        assertThat(issue1).isNotEqualTo(issue2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(IssueDTO.class);
        IssueDTO issueDTO1 = new IssueDTO();
        issueDTO1.setId(1L);
        IssueDTO issueDTO2 = new IssueDTO();
        assertThat(issueDTO1).isNotEqualTo(issueDTO2);
        issueDTO2.setId(issueDTO1.getId());
        assertThat(issueDTO1).isEqualTo(issueDTO2);
        issueDTO2.setId(2L);
        assertThat(issueDTO1).isNotEqualTo(issueDTO2);
        issueDTO1.setId(null);
        assertThat(issueDTO1).isNotEqualTo(issueDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(issueMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(issueMapper.fromId(null)).isNull();
    }
}
