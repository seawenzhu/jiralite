package com.seawen.jiralite.service;

import com.seawen.jiralite.domain.Code;
import com.seawen.jiralite.repository.CodeRepository;
import com.seawen.jiralite.service.dto.CodeDTO;
import com.seawen.jiralite.service.mapper.CodeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Code.
 */
@Service
@Transactional
public class CodeService {

    private final Logger log = LoggerFactory.getLogger(CodeService.class);

    private final CodeRepository codeRepository;

    private final CodeMapper codeMapper;

    public CodeService(CodeRepository codeRepository, CodeMapper codeMapper) {
        this.codeRepository = codeRepository;
        this.codeMapper = codeMapper;
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
        code = codeRepository.save(code);
        return codeMapper.toDto(code);
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
    }
}