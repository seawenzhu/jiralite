import { Route } from '@angular/router';

import { JlConfigurationComponent } from './configuration.component';

export const configurationRoute: Route = {
    path: 'jl-configuration',
    component: JlConfigurationComponent,
    data: {
        pageTitle: 'configuration.title'
    }
};
