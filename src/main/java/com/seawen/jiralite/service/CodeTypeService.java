package com.seawen.jiralite.service;

import com.seawen.jiralite.domain.CodeType;
import com.seawen.jiralite.repository.CodeTypeRepository;
import com.seawen.jiralite.service.dto.CodeTypeDTO;
import com.seawen.jiralite.service.mapper.CodeTypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing CodeType.
 */
@Service
@Transactional
public class CodeTypeService {

    private final Logger log = LoggerFactory.getLogger(CodeTypeService.class);

    private final CodeTypeRepository codeTypeRepository;

    private final CodeTypeMapper codeTypeMapper;

    public CodeTypeService(CodeTypeRepository codeTypeRepository, CodeTypeMapper codeTypeMapper) {
        this.codeTypeRepository = codeTypeRepository;
        this.codeTypeMapper = codeTypeMapper;
    }

    /**
     * Save a codeType.
     *
     * @param codeTypeDTO the entity to save
     * @return the persisted entity
     */
    public CodeTypeDTO save(CodeTypeDTO codeTypeDTO) {
        log.debug("Request to save CodeType : {}", codeTypeDTO);
        CodeType codeType = codeTypeMapper.toEntity(codeTypeDTO);
        codeType = codeTypeRepository.save(codeType);
        return codeTypeMapper.toDto(codeType);
    }

    /**
     *  Get all the codeTypes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<CodeTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CodeTypes");
        return codeTypeRepository.findAll(pageable)
            .map(codeTypeMapper::toDto);
    }

    /**
     *  Get one codeType by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public CodeTypeDTO findOne(Long id) {
        log.debug("Request to get CodeType : {}", id);
        CodeType codeType = codeTypeRepository.findOne(id);
        return codeTypeMapper.toDto(codeType);
    }

    /**
     *  Delete the  codeType by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete CodeType : {}", id);
        codeTypeRepository.delete(id);
    }
}
