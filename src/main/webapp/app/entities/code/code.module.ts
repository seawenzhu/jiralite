import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JiraliteSharedModule } from '../../shared';
import {
    CodeService,
    CodePopupService,
    CodeComponent,
    CodeDetailComponent,
    CodeDialogComponent,
    CodePopupComponent,
    CodeDeletePopupComponent,
    CodeDeleteDialogComponent,
    codeRoute,
    codePopupRoute,
    CodeResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...codeRoute,
    ...codePopupRoute,
];

@NgModule({
    imports: [
        JiraliteSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        CodeComponent,
        CodeDetailComponent,
        CodeDialogComponent,
        CodeDeleteDialogComponent,
        CodePopupComponent,
        CodeDeletePopupComponent,
    ],
    entryComponents: [
        CodeComponent,
        CodeDialogComponent,
        CodePopupComponent,
        CodeDeleteDialogComponent,
        CodeDeletePopupComponent,
    ],
    providers: [
        CodeService,
        CodePopupService,
        CodeResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JiraliteCodeModule {}
