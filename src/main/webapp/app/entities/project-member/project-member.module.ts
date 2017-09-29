import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JiraliteSharedModule } from '../../shared';
import {
    ProjectMemberService,
    ProjectMemberPopupService,
    ProjectMemberComponent,
    ProjectMemberDetailComponent,
    ProjectMemberDialogComponent,
    ProjectMemberPopupComponent,
    ProjectMemberDeletePopupComponent,
    ProjectMemberDeleteDialogComponent,
    projectMemberRoute,
    projectMemberPopupRoute,
    ProjectMemberResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...projectMemberRoute,
    ...projectMemberPopupRoute,
];

@NgModule({
    imports: [
        JiraliteSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        ProjectMemberComponent,
        ProjectMemberDetailComponent,
        ProjectMemberDialogComponent,
        ProjectMemberDeleteDialogComponent,
        ProjectMemberPopupComponent,
        ProjectMemberDeletePopupComponent,
    ],
    entryComponents: [
        ProjectMemberComponent,
        ProjectMemberDialogComponent,
        ProjectMemberPopupComponent,
        ProjectMemberDeleteDialogComponent,
        ProjectMemberDeletePopupComponent,
    ],
    providers: [
        ProjectMemberService,
        ProjectMemberPopupService,
        ProjectMemberResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JiraliteProjectMemberModule {}
