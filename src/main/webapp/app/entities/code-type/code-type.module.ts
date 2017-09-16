import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JiraliteSharedModule } from '../../shared';
import {
    CodeTypeService,
    CodeTypePopupService,
    CodeTypeComponent,
    CodeTypeDetailComponent,
    CodeTypeDialogComponent,
    CodeTypePopupComponent,
    CodeTypeDeletePopupComponent,
    CodeTypeDeleteDialogComponent,
    codeTypeRoute,
    codeTypePopupRoute,
    CodeTypeResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...codeTypeRoute,
    ...codeTypePopupRoute,
];

@NgModule({
    imports: [
        JiraliteSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        CodeTypeComponent,
        CodeTypeDetailComponent,
        CodeTypeDialogComponent,
        CodeTypeDeleteDialogComponent,
        CodeTypePopupComponent,
        CodeTypeDeletePopupComponent,
    ],
    entryComponents: [
        CodeTypeComponent,
        CodeTypeDialogComponent,
        CodeTypePopupComponent,
        CodeTypeDeleteDialogComponent,
        CodeTypeDeletePopupComponent,
    ],
    providers: [
        CodeTypeService,
        CodeTypePopupService,
        CodeTypeResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JiraliteCodeTypeModule {}
