import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { CandidateComponent } from './candidate.component';
import { CandidateDetailComponent } from './candidate-detail.component';
import { CandidatePopupComponent } from './candidate-dialog.component';
import { CandidateDeletePopupComponent } from './candidate-delete-dialog.component';

export const candidateRoute: Routes = [
    {
        path: 'candidate',
        component: CandidateComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Candidates'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'candidate/:id',
        component: CandidateDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Candidates'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const candidatePopupRoute: Routes = [
    {
        path: 'candidate-new',
        component: CandidatePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Candidates'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'candidate/:id/edit',
        component: CandidatePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Candidates'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'candidate/:id/delete',
        component: CandidateDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Candidates'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
