import { Route } from '@angular/router';

import { JlTrackerComponent } from './tracker.component';

export const trackerRoute: Route = {
    path: 'jl-tracker',
    component: JlTrackerComponent,
    data: {
        pageTitle: 'tracker.title'
    }
};
