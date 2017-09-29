"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
Object.defineProperty(exports, "__esModule", { value: true });
var core_1 = require("@angular/core");
var forms_1 = require("@angular/forms");
var code_constants_1 = require("../../../shared/constants/code-constants");
var IssueCreateComponent = (function () {
    function IssueCreateComponent(localStorage, sessionStorage, fb, route, router, dataUtils, alertService, issueService, projectService, eventManager) {
        var _this = this;
        this.localStorage = localStorage;
        this.sessionStorage = sessionStorage;
        this.fb = fb;
        this.route = route;
        this.router = router;
        this.dataUtils = dataUtils;
        this.alertService = alertService;
        this.issueService = issueService;
        this.projectService = projectService;
        this.eventManager = eventManager;
        this.issue = {};
        this.issueStatus = code_constants_1.CodeConstants.TypeCode_ISSUE_STATUS;
        this.typeStatus = code_constants_1.CodeConstants.TypeCode_ISSUE_TYPE;
        this.priorityStatus = code_constants_1.CodeConstants.TypeCode_ISSUE_PRIORITY;
        this.save = function ($event, value) {
            $event.preventDefault();
            for (var key in _this.editForm.controls) {
                if (_this.editForm.controls[key]) {
                    _this.editForm.controls[key].markAsDirty();
                }
            }
            console.log(value);
            _this.isSaving = true;
            if (value.id !== undefined) {
                _this.subscribeToSaveResponse(_this.issueService.update(value));
            }
            else {
                _this.subscribeToSaveResponse(_this.issueService.create(value));
            }
        };
        var token = this.localStorage.retrieve('authenticationToken') || this.sessionStorage.retrieve('authenticationToken');
        this.options = {
            placeholderText: '请输入Issue的详细信息...',
            height: 300,
            imageUploadURL: 'api/common/file/upload',
            requestHeaders: {
                Authorization: 'Bearer ' + token
            },
            toolbarButtons: ['fullscreen', 'bold', 'italic', 'underline', 'strikeThrough', '|', 'fontFamily', 'fontSize', 'color', '|',
                'align', 'insertLink', 'insertImage', 'insertTable', '|', 'insertHR']
        };
    }
    IssueCreateComponent.prototype.receiveStatusCodes = function (recievedCodes) {
        this.statusCodes = recievedCodes;
    };
    IssueCreateComponent.prototype.receiveTypeCodes = function (recievedCodes) {
        this.typeCodes = recievedCodes;
    };
    IssueCreateComponent.prototype.receivePriorityCodes = function (recievedCodes) {
        this.priorityCodes = recievedCodes;
    };
    IssueCreateComponent.prototype.ngOnInit = function () {
        var _this = this;
        this.editForm = this.fb.group({
            issueNo: ['', [forms_1.Validators.required]],
            issueSubject: ['', [forms_1.Validators.required]],
            issueType: ['', [forms_1.Validators.required]],
            issuePriority: ['', [forms_1.Validators.required]],
            issueStatus: ['', [forms_1.Validators.required]],
            remark: ['', [forms_1.Validators.required]]
        });
        this.routeSub = this.route.params.subscribe(function (params) {
            var id = params['id'];
            if (id) {
                _this.issueService.find(id).subscribe(function (issue) {
                    _this.editForm = _this.fb.group({
                        id: [issue.id],
                        version: [issue.version],
                        issueNo: [issue.issueNo, [forms_1.Validators.required]],
                        issueSubject: [issue.issueSubject, [forms_1.Validators.required]],
                        issueType: [issue.issueType, [forms_1.Validators.required]],
                        issuePriority: [issue.issuePriority, [forms_1.Validators.required]],
                        issueStatus: [issue.issueStatus, [forms_1.Validators.required]],
                        remark: [issue.remark, [forms_1.Validators.required]]
                    });
                });
            }
            else {
            }
        });
        this.isSaving = false;
        this.projectService.query()
            .subscribe(function (res) { _this.projects = res.json; }, function (res) { return _this.onError(res.json); });
    };
    IssueCreateComponent.prototype.ngOnDestroy = function () {
        this.routeSub.unsubscribe();
    };
    IssueCreateComponent.prototype.getFormControl = function (name) {
        return this.editForm.controls[name];
    };
    IssueCreateComponent.prototype.byteSize = function (field) {
        return this.dataUtils.byteSize(field);
    };
    IssueCreateComponent.prototype.openFile = function (contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    };
    IssueCreateComponent.prototype.setFileData = function (event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    };
    IssueCreateComponent.prototype.clear = function () {
        // this.activeModal.dismiss('cancel');
    };
    IssueCreateComponent.prototype.subscribeToSaveResponse = function (result) {
        var _this = this;
        result.subscribe(function (res) {
            return _this.onSaveSuccess(res);
        }, function (res) { return _this.onSaveError(); });
    };
    IssueCreateComponent.prototype.onSaveSuccess = function (result) {
        this.eventManager.broadcast({ name: 'issueListModification', content: 'OK' });
        this.isSaving = false;
        // this.activeModal.dismiss(result);
        this.router.navigate(['/dashboard']);
    };
    IssueCreateComponent.prototype.onSaveError = function () {
        this.isSaving = false;
    };
    IssueCreateComponent.prototype.onError = function (error) {
        this.alertService.error(error.message, null, null);
    };
    IssueCreateComponent.prototype.trackProjectById = function (index, item) {
        return item.id;
    };
    IssueCreateComponent = __decorate([
        core_1.Component({
            selector: 'jl-issue-create',
            templateUrl: './issue-create.component.html',
            styles: []
        })
    ], IssueCreateComponent);
    return IssueCreateComponent;
}());
exports.IssueCreateComponent = IssueCreateComponent;
