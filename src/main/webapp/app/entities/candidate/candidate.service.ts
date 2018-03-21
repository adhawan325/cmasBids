import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Candidate } from './candidate.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Candidate>;

@Injectable()
export class CandidateService {

    private resourceUrl =  SERVER_API_URL + 'api/candidates';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/candidates';

    constructor(private http: HttpClient) { }

    create(candidate: Candidate): Observable<EntityResponseType> {
        const copy = this.convert(candidate);
        return this.http.post<Candidate>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(candidate: Candidate): Observable<EntityResponseType> {
        const copy = this.convert(candidate);
        return this.http.put<Candidate>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Candidate>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Candidate[]>> {
        const options = createRequestOption(req);
        return this.http.get<Candidate[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Candidate[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<Candidate[]>> {
        const options = createRequestOption(req);
        return this.http.get<Candidate[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Candidate[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Candidate = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Candidate[]>): HttpResponse<Candidate[]> {
        const jsonResponse: Candidate[] = res.body;
        const body: Candidate[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Candidate.
     */
    private convertItemFromServer(candidate: Candidate): Candidate {
        const copy: Candidate = Object.assign({}, candidate);
        return copy;
    }

    /**
     * Convert a Candidate to a JSON which can be sent to the server.
     */
    private convert(candidate: Candidate): Candidate {
        const copy: Candidate = Object.assign({}, candidate);
        return copy;
    }
}
