
import { Routes } from "@angular/router";
import { UserRouteAccessService } from "../shared/auth/user-route-access-service";
import { DashboardComponent } from "./dashboard/dashboard.component";
import { IssueCreateComponent } from "./dashboard/issue-create/issue-create.component";
import { IssueViewComponent } from "./dashboard/issue-view/issue-view.component";

export const DashboardRoute: Routes = [
    {
        path: 'dashboard',
        component: DashboardComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jiraliteApp.dashboard.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'dashboard/issue-create',
        component: IssueCreateComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jiraliteApp.dashboard.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'dashboard/:id/issue-edit',
        component: IssueCreateComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jiraliteApp.issue.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
    , {
        path: 'dashboard/issue/:id',
        component: IssueViewComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jiraliteApp.issue.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];
