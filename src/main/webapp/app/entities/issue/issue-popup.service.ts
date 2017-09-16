import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { Issue } from './issue.model';
import { IssueService } from './issue.service';

@Injectable()
export class IssuePopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private issueService: IssueService

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
                this.issueService.find(id).subscribe((issue) => {
                    issue.createdDate = this.datePipe
                        .transform(issue.createdDate, 'yyyy-MM-ddTHH:mm:ss');
                    issue.lastModifiedDate = this.datePipe
                        .transform(issue.lastModifiedDate, 'yyyy-MM-ddTHH:mm:ss');
                    this.ngbModalRef = this.issueModalRef(component, issue);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.issueModalRef(component, new Issue());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    issueModalRef(component: Component, issue: Issue): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.issue = issue;
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
