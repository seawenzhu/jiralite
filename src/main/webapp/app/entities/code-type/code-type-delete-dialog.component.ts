import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { CodeType } from './code-type.model';
import { CodeTypePopupService } from './code-type-popup.service';
import { CodeTypeService } from './code-type.service';

@Component({
    selector: 'jl-code-type-delete-dialog',
    templateUrl: './code-type-delete-dialog.component.html'
})
export class CodeTypeDeleteDialogComponent {

    codeType: CodeType;

    constructor(
        private codeTypeService: CodeTypeService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.codeTypeService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'codeTypeListModification',
                content: 'Deleted an codeType'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jl-code-type-delete-popup',
    template: ''
})
export class CodeTypeDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private codeTypePopupService: CodeTypePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.codeTypePopupService
                .open(CodeTypeDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
