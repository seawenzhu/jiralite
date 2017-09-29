import { Route } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JlTrackerComponent } from './tracker.component';
import { JlTrackerService, Principal } from '../../shared';

export const trackerRoute: Route = {
    path: 'jl-tracker',
    component: JlTrackerComponent,
    data: {
        pageTitle: 'tracker.title'
    }
};
