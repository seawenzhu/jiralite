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

@NgModule({
    imports: [
        JiraliteSharedLibsModule,
        JiraliteSharedCommonModule
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
        DatePipe
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]

})
export class JiraliteSharedModule {}
