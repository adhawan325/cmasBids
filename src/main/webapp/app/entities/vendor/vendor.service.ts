import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Vendor } from './vendor.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Vendor>;

@Injectable()
export class VendorService {

    private resourceUrl =  SERVER_API_URL + 'api/vendors';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/vendors';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    create(vendor: Vendor): Observable<EntityResponseType> {
        const copy = this.convert(vendor);
        return this.http.post<Vendor>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(vendor: Vendor): Observable<EntityResponseType> {
        const copy = this.convert(vendor);
        return this.http.put<Vendor>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Vendor>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Vendor[]>> {
        const options = createRequestOption(req);
        return this.http.get<Vendor[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Vendor[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<Vendor[]>> {
        const options = createRequestOption(req);
        return this.http.get<Vendor[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Vendor[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Vendor = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Vendor[]>): HttpResponse<Vendor[]> {
        const jsonResponse: Vendor[] = res.body;
        const body: Vendor[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Vendor.
     */
    private convertItemFromServer(vendor: Vendor): Vendor {
        const copy: Vendor = Object.assign({}, vendor);
        copy.vendorEndDate = this.dateUtils
            .convertLocalDateFromServer(vendor.vendorEndDate);
        return copy;
    }

    /**
     * Convert a Vendor to a JSON which can be sent to the server.
     */
    private convert(vendor: Vendor): Vendor {
        const copy: Vendor = Object.assign({}, vendor);
        copy.vendorEndDate = this.dateUtils
            .convertLocalDateToServer(vendor.vendorEndDate);
        return copy;
    }
}
