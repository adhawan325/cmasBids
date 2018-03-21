import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { EditorModule } from 'primeng/editor';
import { TabViewModule } from 'primeng/tabview';

import { CmasBidsSharedModule } from '../../shared';
import {
    BidService,
    BidPopupService,
    BidComponent,
    BidDetailComponent,
    BidDialogComponent,
    BidPopupComponent,
    BidDeletePopupComponent,
    BidDeleteDialogComponent,
    bidRoute,
    bidPopupRoute,
} from './';

const ENTITY_STATES = [
    ...bidRoute,
    ...bidPopupRoute,
];

@NgModule({
    imports: [
        CmasBidsSharedModule,
        EditorModule,
        TabViewModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        BidComponent,
        BidDetailComponent,
        BidDialogComponent,
        BidDeleteDialogComponent,
        BidPopupComponent,
        BidDeletePopupComponent,
    ],
    entryComponents: [
        BidComponent,
        BidDialogComponent,
        BidPopupComponent,
        BidDeleteDialogComponent,
        BidDeletePopupComponent,
    ],
    providers: [
        BidService,
        BidPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CmasBidsBidModule {}
