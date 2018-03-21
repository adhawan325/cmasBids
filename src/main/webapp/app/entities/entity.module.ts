import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { CmasBidsBidModule } from './bid/bid.module';
import { CmasBidsDepartmentModule } from './department/department.module';
import { CmasBidsBidDocumentModule } from './bid-document/bid-document.module';
import { CmasBidsVendorModule } from './vendor/vendor.module';
import { CmasBidsCandidateModule } from './candidate/candidate.module';
import { CmasBidsVendorUserModule } from './vendor-user/vendor-user.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        CmasBidsBidModule,
        CmasBidsDepartmentModule,
        CmasBidsBidDocumentModule,
        CmasBidsVendorModule,
        CmasBidsCandidateModule,
        CmasBidsVendorUserModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CmasBidsEntityModule {}
