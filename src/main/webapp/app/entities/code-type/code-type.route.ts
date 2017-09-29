import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { CodeTypeComponent } from './code-type.component';
import { CodeTypeDetailComponent } from './code-type-detail.component';
import { CodeTypePopupComponent } from './code-type-dialog.component';
import { CodeTypeDeletePopupComponent } from './code-type-delete-dialog.component';

@Injectable()
export class CodeTypeResolvePagingParams implements Resolve<any> {

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

export const codeTypeRoute: Routes = [
    {
        path: 'code-type',
        component: CodeTypeComponent,
        resolve: {
            'pagingParams': CodeTypeResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jiraliteApp.codeType.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'code-type/:id',
        component: CodeTypeDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jiraliteApp.codeType.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const codeTypePopupRoute: Routes = [
    {
        path: 'code-type-new',
        component: CodeTypePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jiraliteApp.codeType.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'code-type/:id/edit',
        component: CodeTypePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jiraliteApp.codeType.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'code-type/:id/delete',
        component: CodeTypeDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jiraliteApp.codeType.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
