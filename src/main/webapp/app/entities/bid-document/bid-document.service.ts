import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { BidDocument } from './bid-document.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<BidDocument>;

@Injectable()
export class BidDocumentService {

    private resourceUrl =  SERVER_API_URL + 'api/bid-documents';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/bid-documents';

    constructor(private http: HttpClient) { }

    create(bidDocument: BidDocument): Observable<EntityResponseType> {
        const copy = this.convert(bidDocument);
        return this.http.post<BidDocument>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(bidDocument: BidDocument): Observable<EntityResponseType> {
        const copy = this.convert(bidDocument);
        return this.http.put<BidDocument>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<BidDocument>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<BidDocument[]>> {
        const options = createRequestOption(req);
        return this.http.get<BidDocument[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<BidDocument[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<BidDocument[]>> {
        const options = createRequestOption(req);
        return this.http.get<BidDocument[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<BidDocument[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: BidDocument = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<BidDocument[]>): HttpResponse<BidDocument[]> {
        const jsonResponse: BidDocument[] = res.body;
        const body: BidDocument[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to BidDocument.
     */
    private convertItemFromServer(bidDocument: BidDocument): BidDocument {
        const copy: BidDocument = Object.assign({}, bidDocument);
        return copy;
    }

    /**
     * Convert a BidDocument to a JSON which can be sent to the server.
     */
    private convert(bidDocument: BidDocument): BidDocument {
        const copy: BidDocument = Object.assign({}, bidDocument);
        return copy;
    }
}
