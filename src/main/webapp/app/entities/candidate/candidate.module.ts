import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CmasBidsSharedModule } from '../../shared';
import {
    CandidateService,
    CandidatePopupService,
    CandidateComponent,
    CandidateDetailComponent,
    CandidateDialogComponent,
    CandidatePopupComponent,
    CandidateDeletePopupComponent,
    CandidateDeleteDialogComponent,
    candidateRoute,
    candidatePopupRoute,
} from './';

const ENTITY_STATES = [
    ...candidateRoute,
    ...candidatePopupRoute,
];

@NgModule({
    imports: [
        CmasBidsSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        CandidateComponent,
        CandidateDetailComponent,
        CandidateDialogComponent,
        CandidateDeleteDialogComponent,
        CandidatePopupComponent,
        CandidateDeletePopupComponent,
    ],
    entryComponents: [
        CandidateComponent,
        CandidateDialogComponent,
        CandidatePopupComponent,
        CandidateDeleteDialogComponent,
        CandidateDeletePopupComponent,
    ],
    providers: [
        CandidateService,
        CandidatePopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CmasBidsCandidateModule {}
