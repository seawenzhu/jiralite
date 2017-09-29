import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JiraliteSharedModule } from '../../shared';
import {
    IssueService,
    IssuePopupService,
    IssueComponent,
    IssueDetailComponent,
    IssueDialogComponent,
    IssuePopupComponent,
    IssueDeletePopupComponent,
    IssueDeleteDialogComponent,
    issueRoute,
    issuePopupRoute,
} from './';

const ENTITY_STATES = [
    ...issueRoute,
    ...issuePopupRoute,
];

@NgModule({
    imports: [
        JiraliteSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        IssueComponent,
        IssueDetailComponent,
        IssueDialogComponent,
        IssueDeleteDialogComponent,
        IssuePopupComponent,
        IssueDeletePopupComponent,
    ],
    entryComponents: [
        IssueComponent,
        IssueDialogComponent,
        IssuePopupComponent,
        IssueDeleteDialogComponent,
        IssueDeletePopupComponent,
    ],
    providers: [
        IssueService,
        IssuePopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JiraliteIssueModule {}
