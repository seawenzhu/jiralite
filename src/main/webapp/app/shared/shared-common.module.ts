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
import { CodeSelectComponent } from "./code/code-select.component";

@NgModule({
    imports: [
        JiraliteSharedLibsModule
    ],
    declarations: [
        FindLanguageFromKeyPipe,
        JlAlertComponent,
        JlAlertErrorComponent,
        CodeSelectComponent
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
        JlAlertErrorComponent,
        CodeSelectComponent
    ]
})
export class JiraliteSharedCommonModule {}
