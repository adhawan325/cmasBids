import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { VendorUser } from './vendor-user.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<VendorUser>;

@Injectable()
export class VendorUserService {

    private resourceUrl =  SERVER_API_URL + 'api/vendor-users';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/vendor-users';

    constructor(private http: HttpClient) { }

    create(vendorUser: VendorUser): Observable<EntityResponseType> {
        const copy = this.convert(vendorUser);
        return this.http.post<VendorUser>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(vendorUser: VendorUser): Observable<EntityResponseType> {
        const copy = this.convert(vendorUser);
        return this.http.put<VendorUser>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<VendorUser>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<VendorUser[]>> {
        const options = createRequestOption(req);
        return this.http.get<VendorUser[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<VendorUser[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<VendorUser[]>> {
        const options = createRequestOption(req);
        return this.http.get<VendorUser[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<VendorUser[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: VendorUser = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<VendorUser[]>): HttpResponse<VendorUser[]> {
        const jsonResponse: VendorUser[] = res.body;
        const body: VendorUser[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to VendorUser.
     */
    private convertItemFromServer(vendorUser: VendorUser): VendorUser {
        const copy: VendorUser = Object.assign({}, vendorUser);
        return copy;
    }

    /**
     * Convert a VendorUser to a JSON which can be sent to the server.
     */
    private convert(vendorUser: VendorUser): VendorUser {
        const copy: VendorUser = Object.assign({}, vendorUser);
        return copy;
    }
}
