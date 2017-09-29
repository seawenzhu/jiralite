package com.seawen.jiralite.web.rest;

import com.seawen.jiralite.JiraliteApp;

import com.seawen.jiralite.domain.CodeType;
import com.seawen.jiralite.repository.CodeTypeRepository;
import com.seawen.jiralite.service.CodeTypeService;
import com.seawen.jiralite.repository.search.CodeTypeSearchRepository;
import com.seawen.jiralite.service.dto.CodeTypeDTO;
import com.seawen.jiralite.service.mapper.CodeTypeMapper;
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
 * Test class for the CodeTypeResource REST controller.
 *
 * @see CodeTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JiraliteApp.class)
public class CodeTypeResourceIntTest {

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

    private static final String DEFAULT_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_PARENT_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_PARENT_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_REMARK = "AAAAAAAAAA";
    private static final String UPDATED_REMARK = "BBBBBBBBBB";

    @Autowired
    private CodeTypeRepository codeTypeRepository;

    @Autowired
    private CodeTypeMapper codeTypeMapper;

    @Autowired
    private CodeTypeService codeTypeService;

    @Autowired
    private CodeTypeSearchRepository codeTypeSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCodeTypeMockMvc;

    private CodeType codeType;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CodeTypeResource codeTypeResource = new CodeTypeResource(codeTypeService);
        this.restCodeTypeMockMvc = MockMvcBuilders.standaloneSetup(codeTypeResource)
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
    public static CodeType createEntity(EntityManager em) {
        CodeType codeType = new CodeType()
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .version(DEFAULT_VERSION)
            .typeCode(DEFAULT_TYPE_CODE)
            .parentTypeCode(DEFAULT_PARENT_TYPE_CODE)
            .typeName(DEFAULT_TYPE_NAME)
            .remark(DEFAULT_REMARK);
        return codeType;
    }

    @Before
    public void initTest() {
        codeTypeSearchRepository.deleteAll();
        codeType = createEntity(em);
    }

    @Test
    @Transactional
    public void createCodeType() throws Exception {
        int databaseSizeBeforeCreate = codeTypeRepository.findAll().size();

        // Create the CodeType
        CodeTypeDTO codeTypeDTO = codeTypeMapper.toDto(codeType);
        restCodeTypeMockMvc.perform(post("/api/code-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(codeTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the CodeType in the database
        List<CodeType> codeTypeList = codeTypeRepository.findAll();
        assertThat(codeTypeList).hasSize(databaseSizeBeforeCreate + 1);
        CodeType testCodeType = codeTypeList.get(codeTypeList.size() - 1);
        assertThat(testCodeType.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testCodeType.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testCodeType.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testCodeType.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testCodeType.getVersion()).isEqualTo(DEFAULT_VERSION);
        assertThat(testCodeType.getTypeCode()).isEqualTo(DEFAULT_TYPE_CODE);
        assertThat(testCodeType.getParentTypeCode()).isEqualTo(DEFAULT_PARENT_TYPE_CODE);
        assertThat(testCodeType.getTypeName()).isEqualTo(DEFAULT_TYPE_NAME);
        assertThat(testCodeType.getRemark()).isEqualTo(DEFAULT_REMARK);

        // Validate the CodeType in Elasticsearch
        CodeType codeTypeEs = codeTypeSearchRepository.findOne(testCodeType.getId());
        assertThat(codeTypeEs).isEqualToComparingFieldByField(testCodeType);
    }

    @Test
    @Transactional
    public void createCodeTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = codeTypeRepository.findAll().size();

        // Create the CodeType with an existing ID
        codeType.setId(1L);
        CodeTypeDTO codeTypeDTO = codeTypeMapper.toDto(codeType);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCodeTypeMockMvc.perform(post("/api/code-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(codeTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CodeType in the database
        List<CodeType> codeTypeList = codeTypeRepository.findAll();
        assertThat(codeTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = codeTypeRepository.findAll().size();
        // set the field null
        codeType.setTypeCode(null);

        // Create the CodeType, which fails.
        CodeTypeDTO codeTypeDTO = codeTypeMapper.toDto(codeType);

        restCodeTypeMockMvc.perform(post("/api/code-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(codeTypeDTO)))
            .andExpect(status().isBadRequest());

        List<CodeType> codeTypeList = codeTypeRepository.findAll();
        assertThat(codeTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = codeTypeRepository.findAll().size();
        // set the field null
        codeType.setTypeName(null);

        // Create the CodeType, which fails.
        CodeTypeDTO codeTypeDTO = codeTypeMapper.toDto(codeType);

        restCodeTypeMockMvc.perform(post("/api/code-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(codeTypeDTO)))
            .andExpect(status().isBadRequest());

        List<CodeType> codeTypeList = codeTypeRepository.findAll();
        assertThat(codeTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCodeTypes() throws Exception {
        // Initialize the database
        codeTypeRepository.saveAndFlush(codeType);

        // Get all the codeTypeList
        restCodeTypeMockMvc.perform(get("/api/code-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(codeType.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].typeCode").value(hasItem(DEFAULT_TYPE_CODE.toString())))
            .andExpect(jsonPath("$.[*].parentTypeCode").value(hasItem(DEFAULT_PARENT_TYPE_CODE.toString())))
            .andExpect(jsonPath("$.[*].typeName").value(hasItem(DEFAULT_TYPE_NAME.toString())))
            .andExpect(jsonPath("$.[*].remark").value(hasItem(DEFAULT_REMARK.toString())));
    }

    @Test
    @Transactional
    public void getCodeType() throws Exception {
        // Initialize the database
        codeTypeRepository.saveAndFlush(codeType);

        // Get the codeType
        restCodeTypeMockMvc.perform(get("/api/code-types/{id}", codeType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(codeType.getId().intValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION))
            .andExpect(jsonPath("$.typeCode").value(DEFAULT_TYPE_CODE.toString()))
            .andExpect(jsonPath("$.parentTypeCode").value(DEFAULT_PARENT_TYPE_CODE.toString()))
            .andExpect(jsonPath("$.typeName").value(DEFAULT_TYPE_NAME.toString()))
            .andExpect(jsonPath("$.remark").value(DEFAULT_REMARK.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCodeType() throws Exception {
        // Get the codeType
        restCodeTypeMockMvc.perform(get("/api/code-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCodeType() throws Exception {
        // Initialize the database
        codeTypeRepository.saveAndFlush(codeType);
        codeTypeSearchRepository.save(codeType);
        int databaseSizeBeforeUpdate = codeTypeRepository.findAll().size();

        // Update the codeType
        CodeType updatedCodeType = codeTypeRepository.findOne(codeType.getId());
        updatedCodeType
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .version(UPDATED_VERSION)
            .typeCode(UPDATED_TYPE_CODE)
            .parentTypeCode(UPDATED_PARENT_TYPE_CODE)
            .typeName(UPDATED_TYPE_NAME)
            .remark(UPDATED_REMARK);
        CodeTypeDTO codeTypeDTO = codeTypeMapper.toDto(updatedCodeType);

        restCodeTypeMockMvc.perform(put("/api/code-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(codeTypeDTO)))
            .andExpect(status().isOk());

        // Validate the CodeType in the database
        List<CodeType> codeTypeList = codeTypeRepository.findAll();
        assertThat(codeTypeList).hasSize(databaseSizeBeforeUpdate);
        CodeType testCodeType = codeTypeList.get(codeTypeList.size() - 1);
        assertThat(testCodeType.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testCodeType.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testCodeType.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testCodeType.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testCodeType.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testCodeType.getTypeCode()).isEqualTo(UPDATED_TYPE_CODE);
        assertThat(testCodeType.getParentTypeCode()).isEqualTo(UPDATED_PARENT_TYPE_CODE);
        assertThat(testCodeType.getTypeName()).isEqualTo(UPDATED_TYPE_NAME);
        assertThat(testCodeType.getRemark()).isEqualTo(UPDATED_REMARK);

        // Validate the CodeType in Elasticsearch
        CodeType codeTypeEs = codeTypeSearchRepository.findOne(testCodeType.getId());
        assertThat(codeTypeEs).isEqualToComparingFieldByField(testCodeType);
    }

    @Test
    @Transactional
    public void updateNonExistingCodeType() throws Exception {
        int databaseSizeBeforeUpdate = codeTypeRepository.findAll().size();

        // Create the CodeType
        CodeTypeDTO codeTypeDTO = codeTypeMapper.toDto(codeType);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCodeTypeMockMvc.perform(put("/api/code-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(codeTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the CodeType in the database
        List<CodeType> codeTypeList = codeTypeRepository.findAll();
        assertThat(codeTypeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCodeType() throws Exception {
        // Initialize the database
        codeTypeRepository.saveAndFlush(codeType);
        codeTypeSearchRepository.save(codeType);
        int databaseSizeBeforeDelete = codeTypeRepository.findAll().size();

        // Get the codeType
        restCodeTypeMockMvc.perform(delete("/api/code-types/{id}", codeType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean codeTypeExistsInEs = codeTypeSearchRepository.exists(codeType.getId());
        assertThat(codeTypeExistsInEs).isFalse();

        // Validate the database is empty
        List<CodeType> codeTypeList = codeTypeRepository.findAll();
        assertThat(codeTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCodeType() throws Exception {
        // Initialize the database
        codeTypeRepository.saveAndFlush(codeType);
        codeTypeSearchRepository.save(codeType);

        // Search the codeType
        restCodeTypeMockMvc.perform(get("/api/_search/code-types?query=id:" + codeType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(codeType.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].typeCode").value(hasItem(DEFAULT_TYPE_CODE.toString())))
            .andExpect(jsonPath("$.[*].parentTypeCode").value(hasItem(DEFAULT_PARENT_TYPE_CODE.toString())))
            .andExpect(jsonPath("$.[*].typeName").value(hasItem(DEFAULT_TYPE_NAME.toString())))
            .andExpect(jsonPath("$.[*].remark").value(hasItem(DEFAULT_REMARK.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CodeType.class);
        CodeType codeType1 = new CodeType();
        codeType1.setId(1L);
        CodeType codeType2 = new CodeType();
        codeType2.setId(codeType1.getId());
        assertThat(codeType1).isEqualTo(codeType2);
        codeType2.setId(2L);
        assertThat(codeType1).isNotEqualTo(codeType2);
        codeType1.setId(null);
        assertThat(codeType1).isNotEqualTo(codeType2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CodeTypeDTO.class);
        CodeTypeDTO codeTypeDTO1 = new CodeTypeDTO();
        codeTypeDTO1.setId(1L);
        CodeTypeDTO codeTypeDTO2 = new CodeTypeDTO();
        assertThat(codeTypeDTO1).isNotEqualTo(codeTypeDTO2);
        codeTypeDTO2.setId(codeTypeDTO1.getId());
        assertThat(codeTypeDTO1).isEqualTo(codeTypeDTO2);
        codeTypeDTO2.setId(2L);
        assertThat(codeTypeDTO1).isNotEqualTo(codeTypeDTO2);
        codeTypeDTO1.setId(null);
        assertThat(codeTypeDTO1).isNotEqualTo(codeTypeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(codeTypeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(codeTypeMapper.fromId(null)).isNull();
    }
}
