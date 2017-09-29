import { BaseEntity } from './../../shared';

export class Code implements BaseEntity {
    constructor(
        public id?: number,
        public createdBy?: string,
        public createdDate?: any,
        public lastModifiedBy?: string,
        public lastModifiedDate?: any,
        public version?: number,
        public code?: string,
        public name?: string,
        public seqNum?: number,
        public remark?: any,
        public codeTypeId?: number,
    ) {
    }
}
