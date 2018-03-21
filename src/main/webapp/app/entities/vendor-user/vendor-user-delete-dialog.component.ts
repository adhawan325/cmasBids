import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { VendorUser } from './vendor-user.model';
import { VendorUserPopupService } from './vendor-user-popup.service';
import { VendorUserService } from './vendor-user.service';

@Component({
    selector: 'jhi-vendor-user-delete-dialog',
    templateUrl: './vendor-user-delete-dialog.component.html'
})
export class VendorUserDeleteDialogComponent {

    vendorUser: VendorUser;

    constructor(
        private vendorUserService: VendorUserService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.vendorUserService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'vendorUserListModification',
                content: 'Deleted an vendorUser'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-vendor-user-delete-popup',
    template: ''
})
export class VendorUserDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private vendorUserPopupService: VendorUserPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.vendorUserPopupService
                .open(VendorUserDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
