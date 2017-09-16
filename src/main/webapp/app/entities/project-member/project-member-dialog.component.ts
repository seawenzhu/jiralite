import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { ProjectMember } from './project-member.model';
import { ProjectMemberPopupService } from './project-member-popup.service';
import { ProjectMemberService } from './project-member.service';
import { Project, ProjectService } from '../project';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jl-project-member-dialog',
    templateUrl: './project-member-dialog.component.html'
})
export class ProjectMemberDialogComponent implements OnInit {

    projectMember: ProjectMember;
    isSaving: boolean;

    projects: Project[];

    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: JhiDataUtils,
        private alertService: JhiAlertService,
        private projectMemberService: ProjectMemberService,
        private projectService: ProjectService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.projectService.query()
            .subscribe((res: ResponseWrapper) => { this.projects = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.projectMember.id !== undefined) {
            this.subscribeToSaveResponse(
                this.projectMemberService.update(this.projectMember));
        } else {
            this.subscribeToSaveResponse(
                this.projectMemberService.create(this.projectMember));
        }
    }

    private subscribeToSaveResponse(result: Observable<ProjectMember>) {
        result.subscribe((res: ProjectMember) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: ProjectMember) {
        this.eventManager.broadcast({ name: 'projectMemberListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.alertService.error(error.message, null, null);
    }

    trackProjectById(index: number, item: Project) {
        return item.id;
    }
}

@Component({
    selector: 'jl-project-member-popup',
    template: ''
})
export class ProjectMemberPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private projectMemberPopupService: ProjectMemberPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.projectMemberPopupService
                    .open(ProjectMemberDialogComponent as Component, params['id']);
            } else {
                this.projectMemberPopupService
                    .open(ProjectMemberDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
