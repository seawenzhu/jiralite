import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';

import { Issue } from './issue.model';
import { IssueService } from './issue.service';

@Component({
    selector: 'jhi-issue-detail',
    templateUrl: './issue-detail.component.html'
})
export class IssueDetailComponent implements OnInit, OnDestroy {

    issue: Issue;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private dataUtils: JhiDataUtils,
        private issueService: IssueService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInIssues();
    }

    load(id) {
        this.issueService.find(id).subscribe((issue) => {
            this.issue = issue;
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

    registerChangeInIssues() {
        this.eventSubscriber = this.eventManager.subscribe(
            'issueListModification',
            (response) => this.load(this.issue.id)
        );
    }
}
