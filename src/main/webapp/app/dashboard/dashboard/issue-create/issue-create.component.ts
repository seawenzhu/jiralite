import { Component, OnInit } from '@angular/core';
import { Project } from "../../../entities/project/project.model";
import { Issue } from "../../../entities/issue/issue.model";
import { Observable } from "rxjs/Observable";
import { JhiAlertService, JhiDataUtils, JhiEventManager } from "ng-jhipster";
import { IssueService } from "../../../entities/issue/issue.service";
import { ProjectService } from "../../../entities/project/project.service";
import { ResponseWrapper } from "../../../shared/model/response-wrapper.model";

@Component({
  selector: 'jl-issue-create',
  templateUrl: './issue-create.component.html',
  styles: []
})
export class IssueCreateComponent implements OnInit {

    issue: Issue = {};
    isSaving: boolean;

    projects: Project[];

    options = {
        placeholderText: '请输入Issue的详细信息...',
        charCounterCount: true,
        charCounterMax: 1000
    };

    constructor(
        private dataUtils: JhiDataUtils,
        private alertService: JhiAlertService,
        private issueService: IssueService,
        private projectService: ProjectService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.projectService.query()
            .subscribe((res: ResponseWrapper) => { this.projects = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
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

    save() {
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
