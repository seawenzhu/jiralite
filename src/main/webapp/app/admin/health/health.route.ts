import { Route } from '@angular/router';

import { JlHealthCheckComponent } from './health.component';

export const healthRoute: Route = {
    path: 'jl-health',
    component: JlHealthCheckComponent,
    data: {
        pageTitle: 'health.title'
    }
};
