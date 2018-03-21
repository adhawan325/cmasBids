import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CmasBidsSharedModule } from '../../shared';
import {
    VendorUserService,
    VendorUserPopupService,
    VendorUserComponent,
    VendorUserDetailComponent,
    VendorUserDialogComponent,
    VendorUserPopupComponent,
    VendorUserDeletePopupComponent,
    VendorUserDeleteDialogComponent,
    vendorUserRoute,
    vendorUserPopupRoute,
} from './';

const ENTITY_STATES = [
    ...vendorUserRoute,
    ...vendorUserPopupRoute,
];

@NgModule({
    imports: [
        CmasBidsSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        VendorUserComponent,
        VendorUserDetailComponent,
        VendorUserDialogComponent,
        VendorUserDeleteDialogComponent,
        VendorUserPopupComponent,
        VendorUserDeletePopupComponent,
    ],
    entryComponents: [
        VendorUserComponent,
        VendorUserDialogComponent,
        VendorUserPopupComponent,
        VendorUserDeleteDialogComponent,
        VendorUserDeletePopupComponent,
    ],
    providers: [
        VendorUserService,
        VendorUserPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CmasBidsVendorUserModule {}
