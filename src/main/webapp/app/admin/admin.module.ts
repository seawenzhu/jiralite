import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JlTrackerService } from './../shared/tracker/tracker.service';

import { JiraliteSharedModule } from '../shared';
/* jhipster-needle-add-admin-module-import - JHipster will add admin modules imports here */

import {
    adminState,
    AuditsComponent,
    UserMgmtComponent,
    UserDialogComponent,
    UserDeleteDialogComponent,
    UserMgmtDetailComponent,
    UserMgmtDialogComponent,
    UserMgmtDeleteDialogComponent,
    LogsComponent,
    JlMetricsMonitoringModalComponent,
    JlMetricsMonitoringComponent,
    JlHealthModalComponent,
    JlHealthCheckComponent,
    JlConfigurationComponent,
    JlDocsComponent,
    AuditsService,
    JlConfigurationService,
    JlHealthService,
    JlMetricsService,
    JlTrackerComponent,
    LogsService,
    UserResolvePagingParams,
    UserResolve,
    UserModalService
} from './';

@NgModule({
    imports: [
        JiraliteSharedModule,
        RouterModule.forRoot(adminState, { useHash: true }),
        /* jhipster-needle-add-admin-module - JHipster will add admin modules here */
    ],
    declarations: [
        AuditsComponent,
        UserMgmtComponent,
        UserDialogComponent,
        UserDeleteDialogComponent,
        UserMgmtDetailComponent,
        UserMgmtDialogComponent,
        UserMgmtDeleteDialogComponent,
        LogsComponent,
        JlConfigurationComponent,
        JlHealthCheckComponent,
        JlHealthModalComponent,
        JlDocsComponent,
        JlTrackerComponent,
        JlMetricsMonitoringComponent,
        JlMetricsMonitoringModalComponent
    ],
    entryComponents: [
        UserMgmtDialogComponent,
        UserMgmtDeleteDialogComponent,
        JlHealthModalComponent,
        JlMetricsMonitoringModalComponent,
    ],
    providers: [
        AuditsService,
        JlConfigurationService,
        JlHealthService,
        JlMetricsService,
        LogsService,
        JlTrackerService,
        UserResolvePagingParams,
        UserResolve,
        UserModalService
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JiraliteAdminModule {}
