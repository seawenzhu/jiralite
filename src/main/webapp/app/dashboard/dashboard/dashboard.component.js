"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
Object.defineProperty(exports, "__esModule", { value: true });
var core_1 = require("@angular/core");
var pagination_constants_1 = require("../../shared/constants/pagination.constants");
var Subject_1 = require("rxjs/Subject");
var code_constants_1 = require("../../shared/constants/code-constants");
var DashboardComponent = (function () {
    function DashboardComponent(issueService, alertService, dataUtils, eventManager, parseLinks, activatedRoute, principal) {
        var _this = this;
        this.issueService = issueService;
        this.alertService = alertService;
        this.dataUtils = dataUtils;
        this.eventManager = eventManager;
        this.parseLinks = parseLinks;
        this.activatedRoute = activatedRoute;
        this.principal = principal;
        this.isLoading = true;
        this.searchTerm$ = new Subject_1.Subject();
        this.issueStatus = code_constants_1.CodeConstants.TypeCode_ISSUE_STATUS;
        this.typeStatus = code_constants_1.CodeConstants.TypeCode_ISSUE_TYPE;
        this.priorityStatus = code_constants_1.CodeConstants.TypeCode_ISSUE_PRIORITY;
        this.typeLabel = '类型';
        this.statusLabel = '状态';
        this.priorityLabel = '优先级';
        this.issues = [];
        this.itemsPerPage = pagination_constants_1.ITEMS_PER_PAGE;
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
            .map(function (term) { return term.trim(); })
            .flatMap(function (term) { return _this.inputSearch(term); })
            .subscribe(function (res) { return _this.onSuccess(res.json, res.headers); }, function (res) { return _this.onError(res.json); });
    }
    DashboardComponent.prototype.inputSearch = function (term) {
        this.empty();
        if (term) {
            return this.issueService.search({
                query: term,
                page: this.page,
                size: this.itemsPerPage,
                sort: this.sort()
            });
        }
        else {
            return this.issueService.query();
        }
    };
    DashboardComponent.prototype.receiveStatusCodes = function (recievedCodes) {
        this.statusCodes = recievedCodes;
    };
    DashboardComponent.prototype.receiveTypeCodes = function (recievedCodes) {
        this.typeCodes = recievedCodes;
    };
    DashboardComponent.prototype.receivePriorityCodes = function (recievedCodes) {
        this.priorityCodes = recievedCodes;
    };
    DashboardComponent.prototype.typeChange = function ($event, code) {
        if (code) {
            this.typeLabel = code.name;
        }
        else {
            this.typeLabel = '全部';
        }
    };
    DashboardComponent.prototype.statusChange = function ($event, code) {
        if (code) {
            this.statusLabel = code.name;
        }
        else {
            this.statusLabel = '全部';
        }
    };
    DashboardComponent.prototype.priorityChange = function ($event, code) {
        if (code) {
            this.priorityLabel = code.name;
        }
        else {
            this.priorityLabel = '全部';
        }
    };
    DashboardComponent.prototype.loadAll = function () {
        var _this = this;
        this.isLoading = true;
        if (this.currentSearch) {
            this.issueService.search({
                query: this.currentSearch,
                page: this.page,
                size: this.itemsPerPage,
                sort: this.sort()
            }).subscribe(function (res) { return _this.onSuccess(res.json, res.headers); }, function (res) { return _this.onError(res.json); });
            return;
        }
        this.issueService.query({
            page: this.page,
            size: this.itemsPerPage,
            sort: this.sort()
        }).subscribe(function (res) { return _this.onSuccess(res.json, res.headers); }, function (res) { return _this.onError(res.json); });
    };
    DashboardComponent.prototype.reset = function () {
        this.page = 0;
        this.issues = [];
        this.loadAll();
    };
    DashboardComponent.prototype.loadPage = function (page) {
        this.page = page;
        this.loadAll();
    };
    DashboardComponent.prototype.clear = function () {
        this.empty();
        this.loadAll();
    };
    DashboardComponent.prototype.empty = function () {
        this.issues = [];
        this.links = {
            last: 0
        };
        this.page = 0;
        this.predicate = 'id';
        this.reverse = true;
        this.currentSearch = '';
    };
    DashboardComponent.prototype.search = function (query) {
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
    };
    DashboardComponent.prototype.ngOnInit = function () {
        var _this = this;
        this.loadAll();
        this.principal.identity().then(function (account) {
            _this.currentAccount = account;
        });
        this.registerChangeInIssues();
    };
    DashboardComponent.prototype.ngOnDestroy = function () {
        this.eventManager.destroy(this.eventSubscriber);
    };
    DashboardComponent.prototype.trackId = function (index, item) {
        return item.id;
    };
    DashboardComponent.prototype.byteSize = function (field) {
        return this.dataUtils.byteSize(field);
    };
    DashboardComponent.prototype.openFile = function (contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    };
    DashboardComponent.prototype.registerChangeInIssues = function () {
        var _this = this;
        this.eventSubscriber = this.eventManager.subscribe('issueListModification', function (response) { return _this.reset(); });
    };
    DashboardComponent.prototype.sort = function () {
        var result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    };
    DashboardComponent.prototype.onSuccess = function (data, headers) {
        this.isLoading = false;
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = headers.get('X-Total-Count');
        for (var i = 0; i < data.length; i++) {
            this.issues.push(data[i]);
        }
    };
    DashboardComponent.prototype.onError = function (error) {
        this.isLoading = false;
        this.alertService.error(error.message, null, null);
    };
    DashboardComponent = __decorate([
        core_1.Component({
            selector: 'jl-dashboard',
            templateUrl: './dashboard.component.html',
            styleUrls: ['./dashboard.scss']
        })
    ], DashboardComponent);
    return DashboardComponent;
}());
exports.DashboardComponent = DashboardComponent;
