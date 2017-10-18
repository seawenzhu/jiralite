import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { IssueComponent } from './issue.component';
import { IssueDetailComponent } from './issue-detail.component';
import { IssuePopupComponent } from './issue-dialog.component';
import { IssueDeletePopupComponent } from './issue-delete-dialog.component';

export const issueRoute: Routes = [
    {
        path: 'issue',
        component: IssueComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jiraliteApp.issue.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'issue/:id',
        component: IssueDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jiraliteApp.issue.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const issuePopupRoute: Routes = [
    {
        path: 'issue-new',
        component: IssuePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jiraliteApp.issue.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'issue/:id/edit',
        component: IssuePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jiraliteApp.issue.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'issue/:id/delete',
        component: IssueDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jiraliteApp.issue.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
