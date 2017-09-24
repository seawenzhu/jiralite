import { Injectable } from '@angular/core';
import { BaseRequestOptions, URLSearchParams, Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Code } from './code.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class CodeService {

    private resourceUrl = SERVER_API_URL + 'api/codes';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/codes';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(code: Code): Observable<Code> {
        const copy = this.convert(code);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(code: Code): Observable<Code> {
        const copy = this.convert(code);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: number): Observable<Code> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        if (req) {
            if (req.code) {
                options.params.set("code", req.code);
            }
            if (req.typeCode) {
                options.params.set("typeCode", req.typeCode);
            }
        }
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    search(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map((res: any) => this.convertResponse(res));
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        for (let i = 0; i < jsonResponse.length; i++) {
            this.convertItemFromServer(jsonResponse[i]);
        }
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convertItemFromServer(entity: any) {
        entity.createdDate = this.dateUtils
            .convertDateTimeFromServer(entity.createdDate);
        entity.lastModifiedDate = this.dateUtils
            .convertDateTimeFromServer(entity.lastModifiedDate);
    }

    private convert(code: Code): Code {
        const copy: Code = Object.assign({}, code);

        copy.createdDate = this.dateUtils.toDate(code.createdDate);

        copy.lastModifiedDate = this.dateUtils.toDate(code.lastModifiedDate);
        return copy;
    }
}
