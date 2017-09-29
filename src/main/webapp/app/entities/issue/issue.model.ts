import { BaseEntity } from './../../shared';

export class Issue implements BaseEntity {
    constructor(
        public id?: number,
        public createdBy?: string,
        public createdDate?: any,
        public lastModifiedBy?: string,
        public lastModifiedDate?: any,
        public version?: number,
        public issueNo?: string,
        public issueSubject?: string,
        public issueType?: string,
        public issuePriority?: string,
        public issueStatus?: string,
        public reporter?: string,
        public assigner?: string,
        public remark?: any,
        public comments?: BaseEntity[],
        public projectId?: number,
    ) {
    }
}
