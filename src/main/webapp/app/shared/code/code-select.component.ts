import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { CodeService } from "../../entities/code/code.service";
import { Code } from "../../entities/code/code.model";
import { ResponseWrapper } from "../model/response-wrapper.model";
import { ITEMS_PER_PAGE } from "../constants/pagination.constants";

@Component({
    selector: 'jl-code-select',
    templateUrl: './code-select.component.html',
})
export class CodeSelectComponent implements OnInit {

    @Input() typeCode: string;
    @Input() code: string;
    @Output() codes: EventEmitter<Code>;
    page: any;
    itemsPerPage = ITEMS_PER_PAGE;

    constructor(private codeService: CodeService) {
        this.codes = new EventEmitter();
        this.page = 1;
    }
    ngOnInit(): void {
        this.codeService.query({
            typeCode: this.typeCode,
            code: this.code,
            page: this.page - 1,
            size: this.itemsPerPage}).subscribe(
            (res: ResponseWrapper) => this.onSuccess(res.json, res.headers),
            (res: ResponseWrapper) => this.onError(res.json)
        );

        // this.codeService.getCodesByTypeCode(this.typeCode).subscribe(
        //     resp => {
        //        let result =  resp.json();
        //        console.log(result);
        //        this.codes.emit(result);
        //     },
        //     error => {
        //         console.log(error);
        //     }
        // );
    }

    private onSuccess(data, headers) {
        // this.links = this.parseLinks.parse(headers.get('link'));
        // this.totalItems = headers.get('X-Total-Count');
        // this.queryCount = this.totalItems;
        // this.page = pagingParams.page;
        // this.codes = data;
        this.codes.emit(data);
    }
    private onError(error) {
        // this.alertService.error(error.message, null, null);
    }

}
