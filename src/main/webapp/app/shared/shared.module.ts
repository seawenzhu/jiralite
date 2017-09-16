import { NgModule, CUSTOM_ELEMENTS_SCHEMA, forwardRef } from '@angular/core';
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
    JhiTrackerService,
    HasAnyAuthorityDirective,
    JhiLoginModalComponent
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
        JhiLoginModalComponent,
        HasAnyAuthorityDirective
    ],
    providers: [
        LoginService,
        LoginModalService,
        AccountService,
        StateStorageService,
        Principal,
        CSRFService,
        JhiTrackerService,
        AuthServerProvider,
        UserService,
        DatePipe
    ],
    entryComponents: [JhiLoginModalComponent],
    exports: [
        JiraliteSharedCommonModule,
        JhiLoginModalComponent,
        HasAnyAuthorityDirective,
        FroalaEditorModule,
        FroalaViewModule,
        DatePipe
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]

})
export class JiraliteSharedModule {}
