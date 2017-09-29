import { BaseEntity } from './../../shared';

export class Comments implements BaseEntity {
    constructor(
        public id?: number,
        public createdBy?: string,
        public createdDate?: any,
        public lastModifiedBy?: string,
        public lastModifiedDate?: any,
        public version?: number,
        public remark?: any,
        public issueId?: number,
    ) {
    }
}
