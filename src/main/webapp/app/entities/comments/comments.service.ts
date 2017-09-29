import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Comments } from './comments.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class CommentsService {

    private resourceUrl = SERVER_API_URL + 'api/comments';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/comments';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(comments: Comments): Observable<Comments> {
        const copy = this.convert(comments);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(comments: Comments): Observable<Comments> {
        const copy = this.convert(comments);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Comments> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
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
        const result = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            result.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return new ResponseWrapper(res.headers, result, res.status);
    }

    /**
     * Convert a returned JSON object to Comments.
     */
    private convertItemFromServer(json: any): Comments {
        const entity: Comments = Object.assign(new Comments(), json);
        entity.createdDate = this.dateUtils
            .convertDateTimeFromServer(json.createdDate);
        entity.lastModifiedDate = this.dateUtils
            .convertDateTimeFromServer(json.lastModifiedDate);
        return entity;
    }

    /**
     * Convert a Comments to a JSON which can be sent to the server.
     */
    private convert(comments: Comments): Comments {
        const copy: Comments = Object.assign({}, comments);

        copy.createdDate = this.dateUtils.toDate(comments.createdDate);

        copy.lastModifiedDate = this.dateUtils.toDate(comments.lastModifiedDate);
        return copy;
    }
}
