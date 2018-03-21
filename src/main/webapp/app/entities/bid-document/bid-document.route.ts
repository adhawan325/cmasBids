import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { BidDocumentComponent } from './bid-document.component';
import { BidDocumentDetailComponent } from './bid-document-detail.component';
import { BidDocumentPopupComponent } from './bid-document-dialog.component';
import { BidDocumentDeletePopupComponent } from './bid-document-delete-dialog.component';

export const bidDocumentRoute: Routes = [
    {
        path: 'bid-document',
        component: BidDocumentComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'BidDocuments'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'bid-document/:id',
        component: BidDocumentDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'BidDocuments'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const bidDocumentPopupRoute: Routes = [
    {
        path: 'bid-document-new',
        component: BidDocumentPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'BidDocuments'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'bid-document/:id/edit',
        component: BidDocumentPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'BidDocuments'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'bid-document/:id/delete',
        component: BidDocumentDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'BidDocuments'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
