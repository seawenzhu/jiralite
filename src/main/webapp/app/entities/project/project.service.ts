import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Project } from './project.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class ProjectService {

    private resourceUrl = SERVER_API_URL + 'api/projects';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/projects';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(project: Project): Observable<Project> {
        const copy = this.convert(project);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(project: Project): Observable<Project> {
        const copy = this.convert(project);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: number): Observable<Project> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
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

    private convert(project: Project): Project {
        const copy: Project = Object.assign({}, project);

        copy.createdDate = this.dateUtils.toDate(project.createdDate);

        copy.lastModifiedDate = this.dateUtils.toDate(project.lastModifiedDate);
        return copy;
    }
}
