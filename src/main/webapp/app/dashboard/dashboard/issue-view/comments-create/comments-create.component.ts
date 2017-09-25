import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { NgbActiveModal } from "@ng-bootstrap/ng-bootstrap";
import { JhiAlertService, JhiDataUtils, JhiEventManager } from "ng-jhipster";
import { CommentsService } from "../../../../entities/comments/comments.service";
import { IssueService } from "../../../../entities/issue/issue.service";
import { Observable } from "rxjs/Observable";
import { Comments } from "../../../../entities/comments/comments.model";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { LocalStorageService, SessionStorageService } from "ng2-webstorage";
import { NzNotificationService } from "ng-zorro-antd";
import { NzScrollService } from "ng-zorro-antd/src/core/scroll/nz-scroll.service";

@Component({
  selector: 'jl-comments-create',
  templateUrl: './comments-create.component.html',
  styles: []
})
export class CommentsCreateComponent implements OnInit {

    @Input() issueId: number;
    @Input() comment: Comments;
    @Output() editCommentFinished = new EventEmitter<boolean>();

    isSaving: boolean;

    options: Object;

    editForm: FormGroup;

    constructor(
        private localStorage: LocalStorageService,
        private sessionStorage: SessionStorageService,
        private fb: FormBuilder,
        private _notification: NzNotificationService,
        private nzScrollService: NzScrollService,
        private dataUtils: JhiDataUtils,
        private alertService: JhiAlertService,
        private commentsService: CommentsService,
        private issueService: IssueService,
        private eventManager: JhiEventManager
    ) {
        const token = this.localStorage.retrieve('authenticationToken') || this.sessionStorage.retrieve('authenticationToken');
        this.options = {
            placeholderText: '请输入评论...',
            height: 200,
            imageUploadURL: 'api/common/file/upload',
            requestHeaders: {
                Authorization: 'Bearer ' + token
            },
            toolbarButtons: ['fullscreen', 'bold', 'italic', 'underline', 'strikeThrough', '|', 'fontFamily', 'fontSize', 'color', '|',
                'align', 'insertLink', 'insertImage', 'insertTable', '|', 'insertHR']
        };
    }

    ngOnInit() {
        this.isSaving = false;
        this.initCommentForm();
    }

    private initCommentForm() {
        this.editForm = this.fb.group({
            id: [this.comment && this.comment.id],
            issueId: [this.issueId, [Validators.required]],
            remark: [this.comment && this.comment.remark || '', [Validators.required]]
        });
        console.log(this.editForm);
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
        if (value.id) {
            value.version = this.comment.version;
            this.subscribeToSaveResponse(
                this.commentsService.update(value));
        } else {
            this.subscribeToSaveResponse(
                this.commentsService.create(value));
        }
    }

    cancelComment() {
        this.editCommentFinished.emit(true);
    }

    private subscribeToSaveResponse(result: Observable<Comments>) {
        result.subscribe((res: Comments) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Comments) {
        this.eventManager.broadcast({ name: 'commentsListModification', content: 'OK'});
        this.isSaving = false;
        this._notification.success('评论', '评论成功');
        this.editForm.reset();
        this.initCommentForm();
        for (const key in this.editForm.controls) {
            if (this.editForm.controls[ key ]) {
                this.editForm.controls[key].markAsPristine();
            }
        }
        if (!this.comment) {
            this.nzScrollService.scrollTo(window, document.body.scrollHeight);
        } else {
            this.editCommentFinished.emit(true);
        }

    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.alertService.error(error.message, null, null);
    }

}
