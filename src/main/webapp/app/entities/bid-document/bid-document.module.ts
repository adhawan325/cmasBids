import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CmasBidsSharedModule } from '../../shared';
import {
    BidDocumentService,
    BidDocumentPopupService,
    BidDocumentComponent,
    BidDocumentDetailComponent,
    BidDocumentDialogComponent,
    BidDocumentPopupComponent,
    BidDocumentDeletePopupComponent,
    BidDocumentDeleteDialogComponent,
    bidDocumentRoute,
    bidDocumentPopupRoute,
} from './';

const ENTITY_STATES = [
    ...bidDocumentRoute,
    ...bidDocumentPopupRoute,
];

@NgModule({
    imports: [
        CmasBidsSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        BidDocumentComponent,
        BidDocumentDetailComponent,
        BidDocumentDialogComponent,
        BidDocumentDeleteDialogComponent,
        BidDocumentPopupComponent,
        BidDocumentDeletePopupComponent,
    ],
    entryComponents: [
        BidDocumentComponent,
        BidDocumentDialogComponent,
        BidDocumentPopupComponent,
        BidDocumentDeleteDialogComponent,
        BidDocumentDeletePopupComponent,
    ],
    providers: [
        BidDocumentService,
        BidDocumentPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CmasBidsBidDocumentModule {}
