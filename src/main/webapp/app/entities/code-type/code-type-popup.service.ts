import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { CodeType } from './code-type.model';
import { CodeTypeService } from './code-type.service';

@Injectable()
export class CodeTypePopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private codeTypeService: CodeTypeService

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
                this.codeTypeService.find(id).subscribe((codeType) => {
                    codeType.createdDate = this.datePipe
                        .transform(codeType.createdDate, 'yyyy-MM-ddTHH:mm:ss');
                    codeType.lastModifiedDate = this.datePipe
                        .transform(codeType.lastModifiedDate, 'yyyy-MM-ddTHH:mm:ss');
                    this.ngbModalRef = this.codeTypeModalRef(component, codeType);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.codeTypeModalRef(component, new CodeType());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    codeTypeModalRef(component: Component, codeType: CodeType): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.codeType = codeType;
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
