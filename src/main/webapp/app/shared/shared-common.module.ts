import { NgModule, LOCALE_ID } from '@angular/core';
import { Title } from '@angular/platform-browser';

import { WindowRef } from './tracker/window.service';
import {
    JiraliteSharedLibsModule,
    JhiLanguageHelper,
    FindLanguageFromKeyPipe,
    JlAlertComponent,
    JlAlertErrorComponent
} from './';

@NgModule({
    imports: [
        JiraliteSharedLibsModule
    ],
    declarations: [
        FindLanguageFromKeyPipe,
        JlAlertComponent,
        JlAlertErrorComponent
    ],
    providers: [
        JhiLanguageHelper,
        WindowRef,
        Title,
        {
            provide: LOCALE_ID,
            useValue: 'zh-cn'
        },
    ],
    exports: [
        JiraliteSharedLibsModule,
        FindLanguageFromKeyPipe,
        JlAlertComponent,
        JlAlertErrorComponent
    ]
})
export class JiraliteSharedCommonModule {}
