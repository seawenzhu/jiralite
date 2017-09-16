import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { Code } from './code.model';
import { CodeService } from './code.service';

@Injectable()
export class CodePopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private codeService: CodeService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.codeService.find(id).subscribe((code) => {
                    code.createdDate = this.datePipe
                        .transform(code.createdDate, 'yyyy-MM-ddTHH:mm:ss');
                    code.lastModifiedDate = this.datePipe
                        .transform(code.lastModifiedDate, 'yyyy-MM-ddTHH:mm:ss');
                    this.ngbModalRef = this.codeModalRef(component, code);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.codeModalRef(component, new Code());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    codeModalRef(component: Component, code: Code): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.code = code;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
