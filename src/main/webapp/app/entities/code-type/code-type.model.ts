import { BaseEntity } from './../../shared';

export class CodeType implements BaseEntity {
    constructor(
        public id?: number,
        public createdBy?: string,
        public createdDate?: any,
        public lastModifiedBy?: string,
        public lastModifiedDate?: any,
        public version?: number,
        public typeCode?: string,
        public parentTypeCode?: string,
        public typeName?: string,
        public remark?: any,
        public codes?: BaseEntity[],
    ) {
    }
}
