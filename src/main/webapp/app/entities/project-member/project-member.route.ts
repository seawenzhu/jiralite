import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ProjectMemberComponent } from './project-member.component';
import { ProjectMemberDetailComponent } from './project-member-detail.component';
import { ProjectMemberPopupComponent } from './project-member-dialog.component';
import { ProjectMemberDeletePopupComponent } from './project-member-delete-dialog.component';

@Injectable()
export class ProjectMemberResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const projectMemberRoute: Routes = [
    {
        path: 'project-member',
        component: ProjectMemberComponent,
        resolve: {
            'pagingParams': ProjectMemberResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jiraliteApp.projectMember.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'project-member/:id',
        component: ProjectMemberDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jiraliteApp.projectMember.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const projectMemberPopupRoute: Routes = [
    {
        path: 'project-member-new',
        component: ProjectMemberPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jiraliteApp.projectMember.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'project-member/:id/edit',
        component: ProjectMemberPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jiraliteApp.projectMember.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'project-member/:id/delete',
        component: ProjectMemberDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jiraliteApp.projectMember.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
