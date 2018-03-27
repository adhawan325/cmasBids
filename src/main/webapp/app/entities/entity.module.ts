import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { CmasBidsBidModule } from './bid/bid.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        CmasBidsBidModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CmasBidsEntityModule {}
