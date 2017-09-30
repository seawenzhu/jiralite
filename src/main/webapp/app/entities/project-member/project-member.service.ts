import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { ProjectMember } from './project-member.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class ProjectMemberService {

    private resourceUrl = SERVER_API_URL + 'api/project-members';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/project-members';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(projectMember: ProjectMember): Observable<ProjectMember> {
        const copy = this.convert(projectMember);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(projectMember: ProjectMember): Observable<ProjectMember> {
        const copy = this.convert(projectMember);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<ProjectMember> {
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
     * Convert a returned JSON object to ProjectMember.
     */
    private convertItemFromServer(json: any): ProjectMember {
        const entity: ProjectMember = Object.assign(new ProjectMember(), json);
        entity.createdDate = this.dateUtils
            .convertDateTimeFromServer(json.createdDate);
        entity.lastModifiedDate = this.dateUtils
            .convertDateTimeFromServer(json.lastModifiedDate);
        return entity;
    }

    /**
     * Convert a ProjectMember to a JSON which can be sent to the server.
     */
    private convert(projectMember: ProjectMember): ProjectMember {
        const copy: ProjectMember = Object.assign({}, projectMember);

        copy.createdDate = this.dateUtils.toDate(projectMember.createdDate);

        copy.lastModifiedDate = this.dateUtils.toDate(projectMember.lastModifiedDate);
        return copy;
    }
}
