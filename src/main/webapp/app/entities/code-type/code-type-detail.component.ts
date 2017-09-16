import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';

import { CodeType } from './code-type.model';
import { CodeTypeService } from './code-type.service';

@Component({
    selector: 'jl-code-type-detail',
    templateUrl: './code-type-detail.component.html'
})
export class CodeTypeDetailComponent implements OnInit, OnDestroy {

    codeType: CodeType;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private dataUtils: JhiDataUtils,
        private codeTypeService: CodeTypeService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInCodeTypes();
    }

    load(id) {
        this.codeTypeService.find(id).subscribe((codeType) => {
            this.codeType = codeType;
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

    registerChangeInCodeTypes() {
        this.eventSubscriber = this.eventManager.subscribe(
            'codeTypeListModification',
            (response) => this.load(this.codeType.id)
        );
    }
}
