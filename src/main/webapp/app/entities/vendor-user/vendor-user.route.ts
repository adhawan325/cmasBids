import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { VendorUserComponent } from './vendor-user.component';
import { VendorUserDetailComponent } from './vendor-user-detail.component';
import { VendorUserPopupComponent } from './vendor-user-dialog.component';
import { VendorUserDeletePopupComponent } from './vendor-user-delete-dialog.component';

export const vendorUserRoute: Routes = [
    {
        path: 'vendor-user',
        component: VendorUserComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'VendorUsers'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'vendor-user/:id',
        component: VendorUserDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'VendorUsers'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const vendorUserPopupRoute: Routes = [
    {
        path: 'vendor-user-new',
        component: VendorUserPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'VendorUsers'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'vendor-user/:id/edit',
        component: VendorUserPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'VendorUsers'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'vendor-user/:id/delete',
        component: VendorUserDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'VendorUsers'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
