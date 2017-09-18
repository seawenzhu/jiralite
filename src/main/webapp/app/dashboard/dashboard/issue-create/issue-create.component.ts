import { Component, OnDestroy, OnInit } from '@angular/core';
import { Project } from "../../../entities/project/project.model";
import { Issue } from "../../../entities/issue/issue.model";
import { Observable } from "rxjs/Observable";
import { JhiAlertService, JhiDataUtils, JhiEventManager } from "ng-jhipster";
import { IssueService } from "../../../entities/issue/issue.service";
import { ProjectService } from "../../../entities/project/project.service";
import { ResponseWrapper } from "../../../shared/model/response-wrapper.model";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { ActivatedRoute } from "@angular/router";

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

    options = {
        placeholderText: '请输入Issue的详细信息...',
        height: 200
    };

    editForm: FormGroup;

    constructor(
        private fb: FormBuilder,
        private route: ActivatedRoute,
        private dataUtils: JhiDataUtils,
        private alertService: JhiAlertService,
        private issueService: IssueService,
        private projectService: ProjectService,
        private eventManager: JhiEventManager
    ) {
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
        if (this.issue.id !== undefined) {
            this.subscribeToSaveResponse(
                this.issueService.update(this.issue));
        } else {
            this.subscribeToSaveResponse(
                this.issueService.create(this.issue));
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
