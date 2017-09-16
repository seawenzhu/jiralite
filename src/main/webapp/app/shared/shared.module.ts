import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { DatePipe } from '@angular/common';

import {
    JiraliteSharedLibsModule,
    JiraliteSharedCommonModule,
    CSRFService,
    AuthServerProvider,
    AccountService,
    UserService,
    StateStorageService,
    LoginService,
    LoginModalService,
    Principal,
    JlTrackerService,
    HasAnyAuthorityDirective,
    JlLoginModalComponent
} from './';
import { FroalaEditorModule, FroalaViewModule } from "angular-froala-wysiwyg";

import "froala-editor/js/froala_editor.pkgd.min.js";
@NgModule({
    imports: [
        JiraliteSharedLibsModule,
        JiraliteSharedCommonModule,
        FroalaEditorModule,
        FroalaViewModule,
    ],
    declarations: [
        JlLoginModalComponent,
        HasAnyAuthorityDirective
    ],
    providers: [
        LoginService,
        LoginModalService,
        AccountService,
        StateStorageService,
        Principal,
        CSRFService,
        JlTrackerService,
        AuthServerProvider,
        UserService,
        DatePipe
    ],
    entryComponents: [JlLoginModalComponent],
    exports: [
        JiraliteSharedCommonModule,
        JlLoginModalComponent,
        HasAnyAuthorityDirective,
        FroalaEditorModule,
        FroalaViewModule,
        DatePipe
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]

})
export class JiraliteSharedModule {}
