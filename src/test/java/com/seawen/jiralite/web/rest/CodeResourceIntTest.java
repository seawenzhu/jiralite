package com.seawen.jiralite.web.rest;

import com.seawen.jiralite.JiraliteApp;

import com.seawen.jiralite.domain.Code;
import com.seawen.jiralite.repository.CodeRepository;
import com.seawen.jiralite.service.CodeService;
import com.seawen.jiralite.service.dto.CodeDTO;
import com.seawen.jiralite.service.mapper.CodeMapper;
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
 * Test class for the CodeResource REST controller.
 *
 * @see CodeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JiraliteApp.class)
public class CodeResourceIntTest {

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

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_SEQ_NUM = 1;
    private static final Integer UPDATED_SEQ_NUM = 2;

    private static final String DEFAULT_REMARK = "AAAAAAAAAA";
    private static final String UPDATED_REMARK = "BBBBBBBBBB";

    @Autowired
    private CodeRepository codeRepository;

    @Autowired
    private CodeMapper codeMapper;

    @Autowired
    private CodeService codeService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCodeMockMvc;

    private Code code;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CodeResource codeResource = new CodeResource(codeService);
        this.restCodeMockMvc = MockMvcBuilders.standaloneSetup(codeResource)
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
    public static Code createEntity(EntityManager em) {
        Code code = new Code()
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .version(DEFAULT_VERSION)
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .seqNum(DEFAULT_SEQ_NUM)
            .remark(DEFAULT_REMARK);
        return code;
    }

    @Before
    public void initTest() {
        code = createEntity(em);
    }

    @Test
    @Transactional
    public void createCode() throws Exception {
        int databaseSizeBeforeCreate = codeRepository.findAll().size();

        // Create the Code
        CodeDTO codeDTO = codeMapper.toDto(code);
        restCodeMockMvc.perform(post("/api/codes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(codeDTO)))
            .andExpect(status().isCreated());

        // Validate the Code in the database
        List<Code> codeList = codeRepository.findAll();
        assertThat(codeList).hasSize(databaseSizeBeforeCreate + 1);
        Code testCode = codeList.get(codeList.size() - 1);
        assertThat(testCode.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testCode.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testCode.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testCode.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testCode.getVersion()).isEqualTo(DEFAULT_VERSION);
        assertThat(testCode.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testCode.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCode.getSeqNum()).isEqualTo(DEFAULT_SEQ_NUM);
        assertThat(testCode.getRemark()).isEqualTo(DEFAULT_REMARK);
    }

    @Test
    @Transactional
    public void createCodeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = codeRepository.findAll().size();

        // Create the Code with an existing ID
        code.setId(1L);
        CodeDTO codeDTO = codeMapper.toDto(code);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCodeMockMvc.perform(post("/api/codes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(codeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Code in the database
        List<Code> codeList = codeRepository.findAll();
        assertThat(codeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = codeRepository.findAll().size();
        // set the field null
        code.setCode(null);

        // Create the Code, which fails.
        CodeDTO codeDTO = codeMapper.toDto(code);

        restCodeMockMvc.perform(post("/api/codes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(codeDTO)))
            .andExpect(status().isBadRequest());

        List<Code> codeList = codeRepository.findAll();
        assertThat(codeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = codeRepository.findAll().size();
        // set the field null
        code.setName(null);

        // Create the Code, which fails.
        CodeDTO codeDTO = codeMapper.toDto(code);

        restCodeMockMvc.perform(post("/api/codes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(codeDTO)))
            .andExpect(status().isBadRequest());

        List<Code> codeList = codeRepository.findAll();
        assertThat(codeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSeqNumIsRequired() throws Exception {
        int databaseSizeBeforeTest = codeRepository.findAll().size();
        // set the field null
        code.setSeqNum(null);

        // Create the Code, which fails.
        CodeDTO codeDTO = codeMapper.toDto(code);

        restCodeMockMvc.perform(post("/api/codes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(codeDTO)))
            .andExpect(status().isBadRequest());

        List<Code> codeList = codeRepository.findAll();
        assertThat(codeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCodes() throws Exception {
        // Initialize the database
        codeRepository.saveAndFlush(code);

        // Get all the codeList
        restCodeMockMvc.perform(get("/api/codes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(code.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].seqNum").value(hasItem(DEFAULT_SEQ_NUM)))
            .andExpect(jsonPath("$.[*].remark").value(hasItem(DEFAULT_REMARK.toString())));
    }

    @Test
    @Transactional
    public void getCode() throws Exception {
        // Initialize the database
        codeRepository.saveAndFlush(code);

        // Get the code
        restCodeMockMvc.perform(get("/api/codes/{id}", code.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(code.getId().intValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.seqNum").value(DEFAULT_SEQ_NUM))
            .andExpect(jsonPath("$.remark").value(DEFAULT_REMARK.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCode() throws Exception {
        // Get the code
        restCodeMockMvc.perform(get("/api/codes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCode() throws Exception {
        // Initialize the database
        codeRepository.saveAndFlush(code);
        int databaseSizeBeforeUpdate = codeRepository.findAll().size();

        // Update the code
        Code updatedCode = codeRepository.findOne(code.getId());
        updatedCode
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .version(UPDATED_VERSION)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .seqNum(UPDATED_SEQ_NUM)
            .remark(UPDATED_REMARK);
        CodeDTO codeDTO = codeMapper.toDto(updatedCode);

        restCodeMockMvc.perform(put("/api/codes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(codeDTO)))
            .andExpect(status().isOk());

        // Validate the Code in the database
        List<Code> codeList = codeRepository.findAll();
        assertThat(codeList).hasSize(databaseSizeBeforeUpdate);
        Code testCode = codeList.get(codeList.size() - 1);
        assertThat(testCode.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testCode.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testCode.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testCode.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testCode.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testCode.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testCode.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCode.getSeqNum()).isEqualTo(UPDATED_SEQ_NUM);
        assertThat(testCode.getRemark()).isEqualTo(UPDATED_REMARK);
    }

    @Test
    @Transactional
    public void updateNonExistingCode() throws Exception {
        int databaseSizeBeforeUpdate = codeRepository.findAll().size();

        // Create the Code
        CodeDTO codeDTO = codeMapper.toDto(code);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCodeMockMvc.perform(put("/api/codes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(codeDTO)))
            .andExpect(status().isCreated());

        // Validate the Code in the database
        List<Code> codeList = codeRepository.findAll();
        assertThat(codeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCode() throws Exception {
        // Initialize the database
        codeRepository.saveAndFlush(code);
        int databaseSizeBeforeDelete = codeRepository.findAll().size();

        // Get the code
        restCodeMockMvc.perform(delete("/api/codes/{id}", code.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Code> codeList = codeRepository.findAll();
        assertThat(codeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Code.class);
        Code code1 = new Code();
        code1.setId(1L);
        Code code2 = new Code();
        code2.setId(code1.getId());
        assertThat(code1).isEqualTo(code2);
        code2.setId(2L);
        assertThat(code1).isNotEqualTo(code2);
        code1.setId(null);
        assertThat(code1).isNotEqualTo(code2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CodeDTO.class);
        CodeDTO codeDTO1 = new CodeDTO();
        codeDTO1.setId(1L);
        CodeDTO codeDTO2 = new CodeDTO();
        assertThat(codeDTO1).isNotEqualTo(codeDTO2);
        codeDTO2.setId(codeDTO1.getId());
        assertThat(codeDTO1).isEqualTo(codeDTO2);
        codeDTO2.setId(2L);
        assertThat(codeDTO1).isNotEqualTo(codeDTO2);
        codeDTO1.setId(null);
        assertThat(codeDTO1).isNotEqualTo(codeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(codeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(codeMapper.fromId(null)).isNull();
    }
}
