import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { VendorUser } from './vendor-user.model';
import { VendorUserPopupService } from './vendor-user-popup.service';
import { VendorUserService } from './vendor-user.service';
import { Vendor, VendorService } from '../vendor';

@Component({
    selector: 'jhi-vendor-user-dialog',
    templateUrl: './vendor-user-dialog.component.html'
})
export class VendorUserDialogComponent implements OnInit {

    vendorUser: VendorUser;
    isSaving: boolean;

    vendors: Vendor[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private vendorUserService: VendorUserService,
        private vendorService: VendorService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.vendorService.query()
            .subscribe((res: HttpResponse<Vendor[]>) => { this.vendors = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.vendorUser.id !== undefined) {
            this.subscribeToSaveResponse(
                this.vendorUserService.update(this.vendorUser));
        } else {
            this.subscribeToSaveResponse(
                this.vendorUserService.create(this.vendorUser));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<VendorUser>>) {
        result.subscribe((res: HttpResponse<VendorUser>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: VendorUser) {
        this.eventManager.broadcast({ name: 'vendorUserListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackVendorById(index: number, item: Vendor) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-vendor-user-popup',
    template: ''
})
export class VendorUserPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private vendorUserPopupService: VendorUserPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.vendorUserPopupService
                    .open(VendorUserDialogComponent as Component, params['id']);
            } else {
                this.vendorUserPopupService
                    .open(VendorUserDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
