import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Comments } from "../../../../entities/comments/comments.model";
import { Subscription } from "rxjs/Subscription";
import { CommentsService } from "../../../../entities/comments/comments.service";
import { JhiAlertService, JhiDataUtils, JhiEventManager, JhiParseLinks } from "ng-jhipster";
import { ActivatedRoute } from "@angular/router";
import { Principal } from "../../../../shared/auth/principal.service";
import { ITEMS_PER_PAGE } from "../../../../shared/constants/pagination.constants";
import { ResponseWrapper } from "../../../../shared/model/response-wrapper.model";
import { NzModalService } from "ng-zorro-antd";

@Component({
    selector: 'jl-issue-comments',
    templateUrl: './issue-comments.component.html',
    styles: [`
        .gutter-row{
            padding-bottom: 10px;
            margin-bottom: 24px;
            border-bottom: 1px solid #e5e5e5;
        }
    `]
})
export class IssueCommentsComponent implements OnInit, OnDestroy {

    @Input("issueId") issueId: number;

    currentEditComments: Comments;
    comments: Comments[];
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

    constructor(
        private commentsService: CommentsService,
        private alertService: JhiAlertService,
        private dataUtils: JhiDataUtils,
        private eventManager: JhiEventManager,
        private parseLinks: JhiParseLinks,
        private activatedRoute: ActivatedRoute,
        private confirmServ: NzModalService,
        private principal: Principal
    ) {
        this.comments = [];
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.page = 0;
        this.links = {
            last: 0
        };
        this.predicate = 'id';
        this.reverse = true;
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        this.isLoading = true;
        if (this.currentSearch) {
            this.commentsService.search({
                issueId: this.issueId,
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
        this.commentsService.query({
            issueId: this.issueId,
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
        this.comments = [];
        this.loadAll();
    }

    loadPage(page) {
        this.page = page;
        this.loadAll();
    }

    clear() {
        this.comments = [];
        this.links = {
            last: 0
        };
        this.page = 0;
        this.predicate = 'id';
        this.reverse = true;
        this.currentSearch = '';
        this.loadAll();
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.comments = [];
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
        this.registerChangeInComments();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Comments) {
        return item.id;
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    registerChangeInComments() {
        this.eventSubscriber = this.eventManager.subscribe('commentsListModification', (response) => this.reset());
    }

    deleteComment(comments: Comments) {
        const $this = this;
        this.confirmServ.confirm({
            title  : '您是否确认要删除这项内容',
            // content: '点确认 1 秒后关闭',
            onOk() {
                $this.commentsService.delete(comments.id).subscribe((response) => {
                    $this.eventManager.broadcast({
                        name: 'commentsListModification',
                        content: 'Deleted an comments'
                    });
                });
            },
            onCancel() {
            }
        });

    }

    editComment(comments: Comments) {
        this.commentsService.find(comments.id).subscribe((current) => {
            this.currentEditComments = current;
        });
    }

    editCommentFinished(finished: boolean) {
       this.currentEditComments = null;
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
            this.comments.push(data[i]);
        }
    }

    private onError(error) {
        this.isLoading = false;
        this.alertService.error(error.message, null, null);
    }
}
