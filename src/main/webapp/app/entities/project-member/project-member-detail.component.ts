import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';

import { ProjectMember } from './project-member.model';
import { ProjectMemberService } from './project-member.service';

@Component({
    selector: 'jhi-project-member-detail',
    templateUrl: './project-member-detail.component.html'
})
export class ProjectMemberDetailComponent implements OnInit, OnDestroy {

    projectMember: ProjectMember;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private dataUtils: JhiDataUtils,
        private projectMemberService: ProjectMemberService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInProjectMembers();
    }

    load(id) {
        this.projectMemberService.find(id).subscribe((projectMember) => {
            this.projectMember = projectMember;
        });
    }
    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInProjectMembers() {
        this.eventSubscriber = this.eventManager.subscribe(
            'projectMemberListModification',
            (response) => this.load(this.projectMember.id)
        );
    }
}
