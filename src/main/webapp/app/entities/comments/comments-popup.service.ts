import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { Comments } from './comments.model';
import { CommentsService } from './comments.service';

@Injectable()
export class CommentsPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private commentsService: CommentsService

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
                this.commentsService.find(id).subscribe((comments) => {
                    comments.createdDate = this.datePipe
                        .transform(comments.createdDate, 'yyyy-MM-ddTHH:mm:ss');
                    comments.lastModifiedDate = this.datePipe
                        .transform(comments.lastModifiedDate, 'yyyy-MM-ddTHH:mm:ss');
                    this.ngbModalRef = this.commentsModalRef(component, comments);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.commentsModalRef(component, new Comments());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    commentsModalRef(component: Component, comments: Comments): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.comments = comments;
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
