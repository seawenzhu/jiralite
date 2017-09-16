import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { Comments } from './comments.model';
import { CommentsPopupService } from './comments-popup.service';
import { CommentsService } from './comments.service';
import { Issue, IssueService } from '../issue';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-comments-dialog',
    templateUrl: './comments-dialog.component.html'
})
export class CommentsDialogComponent implements OnInit {

    comments: Comments;
    isSaving: boolean;

    issues: Issue[];

    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: JhiDataUtils,
        private alertService: JhiAlertService,
        private commentsService: CommentsService,
        private issueService: IssueService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.issueService.query()
            .subscribe((res: ResponseWrapper) => { this.issues = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
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
        if (this.comments.id !== undefined) {
            this.subscribeToSaveResponse(
                this.commentsService.update(this.comments));
        } else {
            this.subscribeToSaveResponse(
                this.commentsService.create(this.comments));
        }
    }

    private subscribeToSaveResponse(result: Observable<Comments>) {
        result.subscribe((res: Comments) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Comments) {
        this.eventManager.broadcast({ name: 'commentsListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.alertService.error(error.message, null, null);
    }

    trackIssueById(index: number, item: Issue) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-comments-popup',
    template: ''
})
export class CommentsPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private commentsPopupService: CommentsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.commentsPopupService
                    .open(CommentsDialogComponent as Component, params['id']);
            } else {
                this.commentsPopupService
                    .open(CommentsDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
