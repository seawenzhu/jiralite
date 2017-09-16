import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ProjectMember } from './project-member.model';
import { ProjectMemberPopupService } from './project-member-popup.service';
import { ProjectMemberService } from './project-member.service';

@Component({
    selector: 'jhi-project-member-delete-dialog',
    templateUrl: './project-member-delete-dialog.component.html'
})
export class ProjectMemberDeleteDialogComponent {

    projectMember: ProjectMember;

    constructor(
        private projectMemberService: ProjectMemberService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.projectMemberService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'projectMemberListModification',
                content: 'Deleted an projectMember'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-project-member-delete-popup',
    template: ''
})
export class ProjectMemberDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private projectMemberPopupService: ProjectMemberPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.projectMemberPopupService
                .open(ProjectMemberDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
