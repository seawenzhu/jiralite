import { Route } from '@angular/router';

import { JlDocsComponent } from './docs.component';

export const docsRoute: Route = {
    path: 'docs',
    component: JlDocsComponent,
    data: {
        pageTitle: 'global.menu.admin.apidocs'
    }
};
