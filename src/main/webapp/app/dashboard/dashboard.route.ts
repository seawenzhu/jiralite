
import { Routes } from "@angular/router";
import { UserRouteAccessService } from "../shared/auth/user-route-access-service";
import { DashboardComponent } from "./dashboard/dashboard.component";
import { IssueCreateComponent } from "./dashboard/issue-create/issue-create.component";

export const DashboardRoute: Routes = [
    {
        path: 'dashboard',
        component: DashboardComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jiraliteApp.dashoboard.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'dashboard/issue-create',
        component: IssueCreateComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jiraliteApp.dashoboard.home.title'
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
];
