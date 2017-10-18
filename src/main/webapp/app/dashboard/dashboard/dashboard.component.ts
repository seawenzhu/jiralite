import { Component, OnDestroy, OnInit } from '@angular/core';
import { Issue } from "../../entities/issue/issue.model";
import { Subscription } from "rxjs/Subscription";
import { IssueService } from "../../entities/issue/issue.service";
import { JhiAlertService, JhiDataUtils, JhiEventManager, JhiParseLinks } from "ng-jhipster";
import { ActivatedRoute } from "@angular/router";
import { Principal } from "../../shared/auth/principal.service";
import { ITEMS_PER_PAGE } from "../../shared/constants/pagination.constants";
import { ResponseWrapper } from "../../shared/model/response-wrapper.model";
import { Subject } from "rxjs/Subject";
import { Observable } from "rxjs/Observable";
import { Code } from "../../entities/code/code.model";
import { CodeConstants } from "../../shared/constants/code-constants";

@Component({
  selector: 'jl-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.scss']
})
export class DashboardComponent implements OnInit, OnDestroy {

    issues: Issue[];
    currentAccount: any;
    eventSubscriber: Subscription;
    itemsPerPage: number;
    links: any;
    page: any;
    predicate: any;
    queryCount: any;
    reverse: any;
    totalItems: number;
    currentSearch: string;
    isLoading = true;

    searchTerm$ = new Subject<string>();
    issueStatus = CodeConstants.TypeCode_ISSUE_STATUS;
    typeStatus = CodeConstants.TypeCode_ISSUE_TYPE;
    priorityStatus = CodeConstants.TypeCode_ISSUE_PRIORITY;
    typeCodes: Code[];
    statusCodes: Code[];
    priorityCodes: Code[];
    typeLabel = '类型';
    typeCode: string;
    statusLabel = '状态';
    statusCode: string;
    priorityLabel = '优先级';
    priorityCode: string;
    searchValue: string;

    constructor(
        private issueService: IssueService,
        private alertService: JhiAlertService,
        private dataUtils: JhiDataUtils,
        private eventManager: JhiEventManager,
        private parseLinks: JhiParseLinks,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.issues = [];
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.page = 0;
        this.links = {
            last: 0
        };
        this.predicate = 'id';
        this.reverse = true;
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';

        this.searchTerm$
            .debounceTime(400)
            .distinctUntilChanged()
            .map((term: string) => term.trim())
            .flatMap((term: string) => this.inputSearch(term))
            .subscribe(
                (res: ResponseWrapper) => this.onSuccess(res.json, res.headers),
                (res: ResponseWrapper) => this.onError(res.json)
            );
    }

    private inputSearch(term: string) {
        this.empty();
        // if (term) {
            return this.issueService.search({
                query: term,
                typeCode: this.typeCode,
                statusCode: this.statusCode,
                priorityCode: this.priorityCode,
                page: this.page,
                size: this.itemsPerPage,
                sort: this.sort()
            })
        // } else {
        //     return this.issueService.query();
        // }

    }

    receiveStatusCodes(recievedCodes: any[]) {
        this.statusCodes = recievedCodes;
    }

    receiveTypeCodes(recievedCodes: any[]) {
        this.typeCodes = recievedCodes;
    }

    receivePriorityCodes(recievedCodes: any[]) {
        this.priorityCodes = recievedCodes;
    }

    typeChange($event, code: Code) {
        if (code) {
            this.typeLabel = code.name;
            this.typeCode = code.code;
        } else {
            this.typeLabel = '全部';
            this.typeCode = '';
        }
        this.inputSearch(this.searchValue).subscribe(
            (res: ResponseWrapper) => this.onSuccess(res.json, res.headers),
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }

    statusChange($event, code: Code) {
        if (code) {
            this.statusLabel = code.name;
            this.statusCode = code.code;
        } else {
            this.statusLabel = '全部';
            this.statusCode = '';
        }
        this.inputSearch(this.searchValue).subscribe(
            (res: ResponseWrapper) => this.onSuccess(res.json, res.headers),
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }

    priorityChange($event, code: Code) {
        if (code) {
            this.priorityLabel = code.name;
            this.priorityCode = code.code;
        } else {
            this.priorityLabel = '全部';
            this.priorityCode = '';
        }
        this.inputSearch(this.searchValue).subscribe(
            (res: ResponseWrapper) => this.onSuccess(res.json, res.headers),
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }

    loadAll() {
        this.isLoading = true;
        if (this.currentSearch) {
            this.issueService.search({
                query: this.currentSearch,
                page: this.page,
                size: this.itemsPerPage,
                sort: this.sort()
            }).subscribe(
                (res: ResponseWrapper) => this.onSuccess(res.json, res.headers),
                (res: ResponseWrapper) => this.onError(res.json)
            );
            return;
        }
        this.issueService.query({
            page: this.page,
            size: this.itemsPerPage,
            sort: this.sort()
        }).subscribe(
            (res: ResponseWrapper) => this.onSuccess(res.json, res.headers),
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }

    reset() {
        this.page = 0;
        this.issues = [];
        this.loadAll();
    }

    loadPage(page) {
        this.page = page;
        this.loadAll();
    }

    clear() {
        this.empty();
        this.loadAll();
    }

    private empty() {
        this.issues = [];
        this.links = {
            last: 0
        };
        this.page = 0;
        this.predicate = 'id';
        this.reverse = true;
        this.currentSearch = '';
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.issues = [];
        this.links = {
            last: 0
        };
        this.page = 0;
        this.predicate = '_score';
        this.reverse = false;
        this.currentSearch = query;
        this.loadAll();
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInIssues();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Issue) {
        return item.id;
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    registerChangeInIssues() {
        this.eventSubscriber = this.eventManager.subscribe('issueListModification', (response) => this.reset());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private onSuccess(data, headers) {
        this.isLoading = false;
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = headers.get('X-Total-Count');
        for (let i = 0; i < data.length; i++) {
            this.issues.push(data[i]);
        }
    }

    private onError(error) {
        this.isLoading = false;
        this.alertService.error(error.message, null, null);
    }

}
