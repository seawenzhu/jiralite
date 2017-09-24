import { Component, OnDestroy, OnInit } from '@angular/core';
import { Project } from "../../../entities/project/project.model";
import { Issue } from "../../../entities/issue/issue.model";
import { Observable } from "rxjs/Observable";
import { JhiAlertService, JhiDataUtils, JhiEventManager } from "ng-jhipster";
import { IssueService } from "../../../entities/issue/issue.service";
import { ProjectService } from "../../../entities/project/project.service";
import { ResponseWrapper } from "../../../shared/model/response-wrapper.model";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { ActivatedRoute, Router } from "@angular/router";
import { LocalStorageService, SessionStorageService } from "ng2-webstorage";
import { CodeConstants } from "../../../shared/constants/code-constants";
import { Code } from "../../../entities/code/code.model";

@Component({
  selector: 'jl-issue-create',
  templateUrl: './issue-create.component.html',
  styles: []
})
export class IssueCreateComponent implements OnInit, OnDestroy {

    routeSub: any;
    issue: Issue = {};
    isSaving: boolean;

    projects: Project[];

    options: Object;

    editForm: FormGroup;

    issueStatus = CodeConstants.TypeCode_ISSUE_STATUS;
    typeStatus = CodeConstants.TypeCode_ISSUE_TYPE;
    priorityStatus = CodeConstants.TypeCode_ISSUE_PRIORITY;
    statusCodes: Code[];
    typeCodes: Code[];
    priorityCodes: Code[];

    constructor(
        private localStorage: LocalStorageService,
        private sessionStorage: SessionStorageService,
        private fb: FormBuilder,
        private route: ActivatedRoute,
        private router: Router,
        private dataUtils: JhiDataUtils,
        private alertService: JhiAlertService,
        private issueService: IssueService,
        private projectService: ProjectService,
        private eventManager: JhiEventManager
    ) {
        const token = this.localStorage.retrieve('authenticationToken') || this.sessionStorage.retrieve('authenticationToken');
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

    receiveStatusCodes(recievedCodes: any[]) {
        this.statusCodes = recievedCodes;
    }

    receiveTypeCodes(recievedCodes: any[]) {
        this.typeCodes = recievedCodes;
    }

    receivePriorityCodes(recievedCodes: any[]) {
        this.priorityCodes = recievedCodes;
    }

    ngOnInit() {
        this.editForm = this.fb.group({
            issueNo            : [ '', [ Validators.required ] ],
            issueSubject       : [ '', [ Validators.required ] ],
            issueType          : [ '', [ Validators.required ] ],
            issuePriority      : [ '', [ Validators.required ] ],
            issueStatus        : [ '', [ Validators.required ] ],
            remark             : [ '', [ Validators.required ] ]
        });
        this.routeSub = this.route.params.subscribe((params) => {
            const id = params['id'];
            if ( id ) {
                this.issueService.find(id).subscribe((issue) => {
                    this.editForm = this.fb.group({
                        id                 : [ issue.id ],
                        version            : [ issue.version ],
                        issueNo            : [ issue.issueNo, [ Validators.required ] ],
                        issueSubject       : [ issue.issueSubject, [ Validators.required ] ],
                        issueType          : [ issue.issueType, [ Validators.required ] ],
                        issuePriority      : [ issue.issuePriority, [ Validators.required ] ],
                        issueStatus        : [ issue.issueStatus, [ Validators.required ] ],
                        remark             : [ issue.remark, [ Validators.required ] ]
                    });
                });
            } else {

            }
        });

        this.isSaving = false;
        this.projectService.query()
            .subscribe((res: ResponseWrapper) => { this.projects = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }

    getFormControl(name) {
        return this.editForm.controls[ name ];
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    clear() {
        // this.activeModal.dismiss('cancel');
    }

    save = ($event, value) => {
        $event.preventDefault();
        for (const key in this.editForm.controls) {
            if (this.editForm.controls[ key ]) {
                this.editForm.controls[ key ].markAsDirty();
            }

        }
        console.log(value);

        this.isSaving = true;
        if (value.id !== undefined) {
            this.subscribeToSaveResponse(
                this.issueService.update(value));
        } else {
            this.subscribeToSaveResponse(
                this.issueService.create(value));
        }
    }

    private subscribeToSaveResponse(result: Observable<Issue>) {
        result.subscribe((res: Issue) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Issue) {
        this.eventManager.broadcast({ name: 'issueListModification', content: 'OK'});
        this.isSaving = false;
        // this.activeModal.dismiss(result);
        this.router.navigate(['/dashboard']);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.alertService.error(error.message, null, null);
    }

    trackProjectById(index: number, item: Project) {
        return item.id;
    }

}
