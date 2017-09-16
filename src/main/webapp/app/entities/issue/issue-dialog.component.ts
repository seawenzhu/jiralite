import { Component, OnInit, OnDestroy, forwardRef } from '@angular/core';
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
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from "@angular/forms";

@Component({
    selector: 'jhi-issue-dialog',
    templateUrl: './issue-dialog.component.html',
    providers: [
        {
            provide: NG_VALUE_ACCESSOR,
            useExisting: forwardRef(() => IssueDialogComponent),
            multi: true
        }
    ]
})
export class IssueDialogComponent implements OnInit, ControlValueAccessor {

    issue: Issue;
    isSaving: boolean;

    projects: Project[];

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

    onChange = (_) => {};
    onTouched = () => {};

    writeValue(obj: any): void {
        this.issue.remark = obj;
    }

    registerOnChange(fn: any): void {
        this.onChange = fn;
    }

    registerOnTouched(fn: any): void {
       this.onTouched = fn;
    }

    setDisabledState?(isDisabled: boolean): void {
        throw new Error("Method not implemented.");
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
    selector: 'jhi-issue-popup',
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
