import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { Code } from './code.model';
import { CodePopupService } from './code-popup.service';
import { CodeService } from './code.service';
import { CodeType, CodeTypeService } from '../code-type';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jl-code-dialog',
    templateUrl: './code-dialog.component.html'
})
export class CodeDialogComponent implements OnInit {

    code: Code;
    isSaving: boolean;

    codetypes: CodeType[];

    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: JhiDataUtils,
        private alertService: JhiAlertService,
        private codeService: CodeService,
        private codeTypeService: CodeTypeService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.codeTypeService.query()
            .subscribe((res: ResponseWrapper) => { this.codetypes = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
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
        if (this.code.id !== undefined) {
            this.subscribeToSaveResponse(
                this.codeService.update(this.code));
        } else {
            this.subscribeToSaveResponse(
                this.codeService.create(this.code));
        }
    }

    private subscribeToSaveResponse(result: Observable<Code>) {
        result.subscribe((res: Code) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Code) {
        this.eventManager.broadcast({ name: 'codeListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.alertService.error(error.message, null, null);
    }

    trackCodeTypeById(index: number, item: CodeType) {
        return item.id;
    }
}

@Component({
    selector: 'jl-code-popup',
    template: ''
})
export class CodePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private codePopupService: CodePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.codePopupService
                    .open(CodeDialogComponent as Component, params['id']);
            } else {
                this.codePopupService
                    .open(CodeDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
