import { BaseEntity } from './../../shared';

export class ProjectMember implements BaseEntity {
    constructor(
        public id?: number,
        public createdBy?: string,
        public createdDate?: any,
        public lastModifiedBy?: string,
        public lastModifiedDate?: any,
        public version?: number,
        public name?: string,
        public remark?: any,
        public projectId?: number,
    ) {
    }
}
