import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { Issue } from './issue.model';
import { IssuePopupService } from './issue-popup.service';
import { IssueService } from './issue.service';
import { Project, ProjectService } from '../project';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jl-issue-dialog',
    templateUrl: './issue-dialog.component.html'
})
export class IssueDialogComponent implements OnInit {

    issue: Issue;
    isSaving: boolean;

    projects: Project[];

    options = {
        placeholderText: '请输入Issue的详细信息...',
        charCounterCount: true,
        charCounterMax: 1000
    };

    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: JhiDataUtils,
        private alertService: JhiAlertService,
        private issueService: IssueService,
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
        if (this.issue.id !== undefined) {
            this.subscribeToSaveResponse(
                this.issueService.update(this.issue));
        } else {
            this.subscribeToSaveResponse(
                this.issueService.create(this.issue));
        }
    }

    private subscribeToSaveResponse(result: Observable<Issue>) {
        result.subscribe((res: Issue) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Issue) {
        this.eventManager.broadcast({ name: 'issueListModification', content: 'OK'});
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
    selector: 'jl-issue-popup',
    template: ''
})
export class IssuePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private issuePopupService: IssuePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.issuePopupService
                    .open(IssueDialogComponent as Component, params['id']);
            } else {
                this.issuePopupService
                    .open(IssueDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
