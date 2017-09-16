import { BaseEntity } from './../../shared';

export class Project implements BaseEntity {
    constructor(
        public id?: number,
        public createdBy?: string,
        public createdDate?: any,
        public lastModifiedBy?: string,
        public lastModifiedDate?: any,
        public version?: number,
        public code?: string,
        public name?: string,
        public remark?: any,
        public projectMembers?: BaseEntity[],
        public issues?: BaseEntity[],
    ) {
    }
}
