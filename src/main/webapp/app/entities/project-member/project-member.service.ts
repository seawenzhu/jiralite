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

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(projectMember: ProjectMember): Observable<ProjectMember> {
        const copy = this.convert(projectMember);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(projectMember: ProjectMember): Observable<ProjectMember> {
        const copy = this.convert(projectMember);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: number): Observable<ProjectMember> {
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

    private convert(projectMember: ProjectMember): ProjectMember {
        const copy: ProjectMember = Object.assign({}, projectMember);

        copy.createdDate = this.dateUtils.toDate(projectMember.createdDate);

        copy.lastModifiedDate = this.dateUtils.toDate(projectMember.lastModifiedDate);
        return copy;
    }
}
