import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { JiraliteCodeModule } from './code/code.module';
import { JiraliteCodeTypeModule } from './code-type/code-type.module';
import { JiraliteProjectModule } from './project/project.module';
import { JiraliteProjectMemberModule } from './project-member/project-member.module';
import { JiraliteIssueModule } from './issue/issue.module';
import { JiraliteCommentsModule } from './comments/comments.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        JiraliteCodeModule,
        JiraliteCodeTypeModule,
        JiraliteProjectModule,
        JiraliteProjectMemberModule,
        JiraliteIssueModule,
        JiraliteCommentsModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JiraliteEntityModule {}
