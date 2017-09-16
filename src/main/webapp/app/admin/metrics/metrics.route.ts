import { Route } from '@angular/router';

import { JlMetricsMonitoringComponent } from './metrics.component';

export const metricsRoute: Route = {
    path: 'jl-metrics',
    component: JlMetricsMonitoringComponent,
    data: {
        pageTitle: 'metrics.title'
    }
};
