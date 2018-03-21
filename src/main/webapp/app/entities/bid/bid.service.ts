import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Bid } from './bid.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Bid>;

@Injectable()
export class BidService {

    private resourceUrl =  SERVER_API_URL + 'api/bids';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/bids';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    create(bid: Bid): Observable<EntityResponseType> {
        const copy = this.convert(bid);
        return this.http.post<Bid>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(bid: Bid): Observable<EntityResponseType> {
        const copy = this.convert(bid);
        return this.http.put<Bid>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Bid>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Bid[]>> {
        const options = createRequestOption(req);
        return this.http.get<Bid[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Bid[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<Bid[]>> {
        const options = createRequestOption(req);
        return this.http.get<Bid[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Bid[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Bid = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Bid[]>): HttpResponse<Bid[]> {
        const jsonResponse: Bid[] = res.body;
        const body: Bid[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Bid.
     */
    private convertItemFromServer(bid: Bid): Bid {
        const copy: Bid = Object.assign({}, bid);
        copy.endDate = this.dateUtils
            .convertLocalDateFromServer(bid.endDate);
        copy.createdOn = this.dateUtils
            .convertLocalDateFromServer(bid.createdOn);
        copy.modifiedOn = this.dateUtils
            .convertLocalDateFromServer(bid.modifiedOn);
        return copy;
    }

    /**
     * Convert a Bid to a JSON which can be sent to the server.
     */
    private convert(bid: Bid): Bid {
        const copy: Bid = Object.assign({}, bid);
        copy.endDate = this.dateUtils
            .convertLocalDateToServer(bid.endDate);
        copy.createdOn = this.dateUtils
            .convertLocalDateToServer(bid.createdOn);
        copy.modifiedOn = this.dateUtils
            .convertLocalDateToServer(bid.modifiedOn);
        return copy;
    }
}
