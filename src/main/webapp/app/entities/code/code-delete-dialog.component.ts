import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Code } from './code.model';
import { CodePopupService } from './code-popup.service';
import { CodeService } from './code.service';

@Component({
    selector: 'jl-code-delete-dialog',
    templateUrl: './code-delete-dialog.component.html'
})
export class CodeDeleteDialogComponent {

    code: Code;

    constructor(
        private codeService: CodeService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.codeService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'codeListModification',
                content: 'Deleted an code'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jl-code-delete-popup',
    template: ''
})
export class CodeDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private codePopupService: CodePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.codePopupService
                .open(CodeDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
