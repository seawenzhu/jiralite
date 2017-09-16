import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { CodeType } from './code-type.model';
import { CodeTypePopupService } from './code-type-popup.service';
import { CodeTypeService } from './code-type.service';

@Component({
    selector: 'jl-code-type-dialog',
    templateUrl: './code-type-dialog.component.html'
})
export class CodeTypeDialogComponent implements OnInit {

    codeType: CodeType;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: JhiDataUtils,
        private alertService: JhiAlertService,
        private codeTypeService: CodeTypeService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
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
        if (this.codeType.id !== undefined) {
            this.subscribeToSaveResponse(
                this.codeTypeService.update(this.codeType));
        } else {
            this.subscribeToSaveResponse(
                this.codeTypeService.create(this.codeType));
        }
    }

    private subscribeToSaveResponse(result: Observable<CodeType>) {
        result.subscribe((res: CodeType) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: CodeType) {
        this.eventManager.broadcast({ name: 'codeTypeListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.alertService.error(error.message, null, null);
    }
}

@Component({
    selector: 'jl-code-type-popup',
    template: ''
})
export class CodeTypePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private codeTypePopupService: CodeTypePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.codeTypePopupService
                    .open(CodeTypeDialogComponent as Component, params['id']);
            } else {
                this.codeTypePopupService
                    .open(CodeTypeDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
