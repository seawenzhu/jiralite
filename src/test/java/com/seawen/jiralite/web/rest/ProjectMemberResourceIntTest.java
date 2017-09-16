package com.seawen.jiralite.web.rest;

import com.seawen.jiralite.JiraliteApp;

import com.seawen.jiralite.domain.ProjectMember;
import com.seawen.jiralite.repository.ProjectMemberRepository;
import com.seawen.jiralite.service.ProjectMemberService;
import com.seawen.jiralite.service.dto.ProjectMemberDTO;
import com.seawen.jiralite.service.mapper.ProjectMemberMapper;
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
 * Test class for the ProjectMemberResource REST controller.
 *
 * @see ProjectMemberResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JiraliteApp.class)
public class ProjectMemberResourceIntTest {

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

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_REMARK = "AAAAAAAAAA";
    private static final String UPDATED_REMARK = "BBBBBBBBBB";

    @Autowired
    private ProjectMemberRepository projectMemberRepository;

    @Autowired
    private ProjectMemberMapper projectMemberMapper;

    @Autowired
    private ProjectMemberService projectMemberService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProjectMemberMockMvc;

    private ProjectMember projectMember;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProjectMemberResource projectMemberResource = new ProjectMemberResource(projectMemberService);
        this.restProjectMemberMockMvc = MockMvcBuilders.standaloneSetup(projectMemberResource)
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
    public static ProjectMember createEntity(EntityManager em) {
        ProjectMember projectMember = new ProjectMember()
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .version(DEFAULT_VERSION)
            .name(DEFAULT_NAME)
            .remark(DEFAULT_REMARK);
        return projectMember;
    }

    @Before
    public void initTest() {
        projectMember = createEntity(em);
    }

    @Test
    @Transactional
    public void createProjectMember() throws Exception {
        int databaseSizeBeforeCreate = projectMemberRepository.findAll().size();

        // Create the ProjectMember
        ProjectMemberDTO projectMemberDTO = projectMemberMapper.toDto(projectMember);
        restProjectMemberMockMvc.perform(post("/api/project-members")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectMemberDTO)))
            .andExpect(status().isCreated());

        // Validate the ProjectMember in the database
        List<ProjectMember> projectMemberList = projectMemberRepository.findAll();
        assertThat(projectMemberList).hasSize(databaseSizeBeforeCreate + 1);
        ProjectMember testProjectMember = projectMemberList.get(projectMemberList.size() - 1);
        assertThat(testProjectMember.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testProjectMember.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testProjectMember.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testProjectMember.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testProjectMember.getVersion()).isEqualTo(DEFAULT_VERSION);
        assertThat(testProjectMember.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProjectMember.getRemark()).isEqualTo(DEFAULT_REMARK);
    }

    @Test
    @Transactional
    public void createProjectMemberWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = projectMemberRepository.findAll().size();

        // Create the ProjectMember with an existing ID
        projectMember.setId(1L);
        ProjectMemberDTO projectMemberDTO = projectMemberMapper.toDto(projectMember);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProjectMemberMockMvc.perform(post("/api/project-members")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectMemberDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProjectMember in the database
        List<ProjectMember> projectMemberList = projectMemberRepository.findAll();
        assertThat(projectMemberList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = projectMemberRepository.findAll().size();
        // set the field null
        projectMember.setName(null);

        // Create the ProjectMember, which fails.
        ProjectMemberDTO projectMemberDTO = projectMemberMapper.toDto(projectMember);

        restProjectMemberMockMvc.perform(post("/api/project-members")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectMemberDTO)))
            .andExpect(status().isBadRequest());

        List<ProjectMember> projectMemberList = projectMemberRepository.findAll();
        assertThat(projectMemberList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProjectMembers() throws Exception {
        // Initialize the database
        projectMemberRepository.saveAndFlush(projectMember);

        // Get all the projectMemberList
        restProjectMemberMockMvc.perform(get("/api/project-members?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projectMember.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].remark").value(hasItem(DEFAULT_REMARK.toString())));
    }

    @Test
    @Transactional
    public void getProjectMember() throws Exception {
        // Initialize the database
        projectMemberRepository.saveAndFlush(projectMember);

        // Get the projectMember
        restProjectMemberMockMvc.perform(get("/api/project-members/{id}", projectMember.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(projectMember.getId().intValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.remark").value(DEFAULT_REMARK.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProjectMember() throws Exception {
        // Get the projectMember
        restProjectMemberMockMvc.perform(get("/api/project-members/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProjectMember() throws Exception {
        // Initialize the database
        projectMemberRepository.saveAndFlush(projectMember);
        int databaseSizeBeforeUpdate = projectMemberRepository.findAll().size();

        // Update the projectMember
        ProjectMember updatedProjectMember = projectMemberRepository.findOne(projectMember.getId());
        updatedProjectMember
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .version(UPDATED_VERSION)
            .name(UPDATED_NAME)
            .remark(UPDATED_REMARK);
        ProjectMemberDTO projectMemberDTO = projectMemberMapper.toDto(updatedProjectMember);

        restProjectMemberMockMvc.perform(put("/api/project-members")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectMemberDTO)))
            .andExpect(status().isOk());

        // Validate the ProjectMember in the database
        List<ProjectMember> projectMemberList = projectMemberRepository.findAll();
        assertThat(projectMemberList).hasSize(databaseSizeBeforeUpdate);
        ProjectMember testProjectMember = projectMemberList.get(projectMemberList.size() - 1);
        assertThat(testProjectMember.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testProjectMember.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testProjectMember.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testProjectMember.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testProjectMember.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testProjectMember.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProjectMember.getRemark()).isEqualTo(UPDATED_REMARK);
    }

    @Test
    @Transactional
    public void updateNonExistingProjectMember() throws Exception {
        int databaseSizeBeforeUpdate = projectMemberRepository.findAll().size();

        // Create the ProjectMember
        ProjectMemberDTO projectMemberDTO = projectMemberMapper.toDto(projectMember);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProjectMemberMockMvc.perform(put("/api/project-members")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectMemberDTO)))
            .andExpect(status().isCreated());

        // Validate the ProjectMember in the database
        List<ProjectMember> projectMemberList = projectMemberRepository.findAll();
        assertThat(projectMemberList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteProjectMember() throws Exception {
        // Initialize the database
        projectMemberRepository.saveAndFlush(projectMember);
        int databaseSizeBeforeDelete = projectMemberRepository.findAll().size();

        // Get the projectMember
        restProjectMemberMockMvc.perform(delete("/api/project-members/{id}", projectMember.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ProjectMember> projectMemberList = projectMemberRepository.findAll();
        assertThat(projectMemberList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProjectMember.class);
        ProjectMember projectMember1 = new ProjectMember();
        projectMember1.setId(1L);
        ProjectMember projectMember2 = new ProjectMember();
        projectMember2.setId(projectMember1.getId());
        assertThat(projectMember1).isEqualTo(projectMember2);
        projectMember2.setId(2L);
        assertThat(projectMember1).isNotEqualTo(projectMember2);
        projectMember1.setId(null);
        assertThat(projectMember1).isNotEqualTo(projectMember2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProjectMemberDTO.class);
        ProjectMemberDTO projectMemberDTO1 = new ProjectMemberDTO();
        projectMemberDTO1.setId(1L);
        ProjectMemberDTO projectMemberDTO2 = new ProjectMemberDTO();
        assertThat(projectMemberDTO1).isNotEqualTo(projectMemberDTO2);
        projectMemberDTO2.setId(projectMemberDTO1.getId());
        assertThat(projectMemberDTO1).isEqualTo(projectMemberDTO2);
        projectMemberDTO2.setId(2L);
        assertThat(projectMemberDTO1).isNotEqualTo(projectMemberDTO2);
        projectMemberDTO1.setId(null);
        assertThat(projectMemberDTO1).isNotEqualTo(projectMemberDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(projectMemberMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(projectMemberMapper.fromId(null)).isNull();
    }
}
