package com.seawen.jiralite.service.mapper;

import com.seawen.jiralite.domain.*;
import com.seawen.jiralite.service.dto.CodeTypeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity CodeType and its DTO CodeTypeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CodeTypeMapper extends EntityMapper<CodeTypeDTO, CodeType> {

    

    @Mapping(target = "codes", ignore = true)
    CodeType toEntity(CodeTypeDTO codeTypeDTO);

    default CodeType fromId(Long id) {
        if (id == null) {
            return null;
        }
        CodeType codeType = new CodeType();
        codeType.setId(id);
        return codeType;
    }
}
