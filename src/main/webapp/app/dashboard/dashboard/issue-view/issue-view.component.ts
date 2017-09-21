import { Component, OnDestroy, OnInit } from '@angular/core';
import { Project } from "../../../entities/project/project.model";
import { Issue } from "../../../entities/issue/issue.model";
import { Subscription } from "rxjs/Subscription";
import { JhiDataUtils, JhiEventManager } from "ng-jhipster";
import { IssueService } from "../../../entities/issue/issue.service";
import { ActivatedRoute } from "@angular/router";

@Component({
  selector: 'jl-issue-view',
  templateUrl: './issue-view.component.html',
    styleUrls: ['./issue-view.scss']
})
export class IssueViewComponent implements OnInit, OnDestroy {

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
