import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { BidComponent } from './bid.component';
import { BidDetailComponent } from './bid-detail.component';
import { BidPopupComponent } from './bid-dialog.component';
import { BidDeletePopupComponent } from './bid-delete-dialog.component';

export const bidRoute: Routes = [
    {
        path: 'bid',
        component: BidComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Bids'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'bid/:id',
        component: BidDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Bids'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const bidPopupRoute: Routes = [
    {
        path: 'bid-new',
        component: BidPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Bids'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'bid/:id/edit',
        component: BidPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Bids'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'bid/:id/delete',
        component: BidDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Bids'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
