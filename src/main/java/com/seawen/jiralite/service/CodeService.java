package com.seawen.jiralite.service;

import com.seawen.jiralite.domain.Code;
import com.seawen.jiralite.domain.CodeType;
import com.seawen.jiralite.repository.CodeRepository;
import com.seawen.jiralite.repository.CodeTypeRepository;
import com.seawen.jiralite.repository.search.CodeSearchRepository;
import com.seawen.jiralite.service.dto.CodeDTO;
import com.seawen.jiralite.service.mapper.CodeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Code.
 */
@Service
@Transactional
public class CodeService {

    private final Logger log = LoggerFactory.getLogger(CodeService.class);

    private final CodeRepository codeRepository;

    private final CodeMapper codeMapper;

    private final CodeSearchRepository codeSearchRepository;

    @Autowired
    private CodeTypeRepository codeTypeRepository;

    public CodeService(CodeRepository codeRepository, CodeMapper codeMapper, CodeSearchRepository codeSearchRepository) {
        this.codeRepository = codeRepository;
        this.codeMapper = codeMapper;
        this.codeSearchRepository = codeSearchRepository;
    }

    /**
     * Save a code.
     *
     * @param codeDTO the entity to save
     * @return the persisted entity
     */
    public CodeDTO save(CodeDTO codeDTO) {
        log.debug("Request to save Code : {}", codeDTO);
        Code code = codeMapper.toEntity(codeDTO);
        code.setCodeType(codeTypeRepository.findOne(codeDTO.getCodeTypeId()));
        code = codeRepository.save(code);
        CodeDTO result = codeMapper.toDto(code);
        codeSearchRepository.save(code);
        return result;
    }

    /**
     *  Get all the codes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<CodeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Codes");
        return codeRepository.findAll(pageable)
            .map(codeMapper::toDto);
    }

    /**
     * Get all the codes by typeCode and Code.
     *
     * @param typeCode
     * @param code
     * @param pageable
     * @return
     */
    @Transactional(readOnly = true)
    public Page<CodeDTO> findAllByTypeCodeAndCode(String typeCode, String code, Pageable pageable) {
        log.debug("Request to get all Codes");
        if (!StringUtils.isEmpty(typeCode) && !StringUtils.isEmpty(code)) {

            return codeRepository.findByCodeTypeTypeCodeAndCodeLike(typeCode, code + "%", pageable)
                .map(codeMapper::toDto);
        } else if (StringUtils.isEmpty(typeCode) && !StringUtils.isEmpty(code)) {

            return codeRepository.findByCodeLike(code, pageable).map(codeMapper::toDto);
        } else if (!StringUtils.isEmpty(typeCode) && StringUtils.isEmpty(code)) {

            return codeRepository.findByCodeTypeTypeCode(typeCode, pageable).map(codeMapper::toDto);
        } else {
            return codeRepository.findAll(pageable).map(codeMapper::toDto);
        }
    }

    /**
     *  Get one code by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public CodeDTO findOne(Long id) {
        log.debug("Request to get Code : {}", id);
        Code code = codeRepository.findOne(id);
        return codeMapper.toDto(code);
    }

    /**
     *  Delete the  code by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Code : {}", id);
        codeRepository.delete(id);
        codeSearchRepository.delete(id);
    }

    /**
     * Search for the code corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<CodeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Codes for query {}", query);
        Page<Code> result = codeSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(codeMapper::toDto);
    }
}
