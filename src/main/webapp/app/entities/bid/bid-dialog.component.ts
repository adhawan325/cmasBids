import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { Bid } from './bid.model';
import { BidPopupService } from './bid-popup.service';
import { BidService } from './bid.service';
import { Department, DepartmentService } from '../department';
import { Vendor, VendorService } from '../vendor';
import { Candidate, CandidateService } from '../candidate';

@Component({
    selector: 'jhi-bid-dialog',
    templateUrl: './bid-dialog.component.html'
})
export class BidDialogComponent implements OnInit {

    bid: Bid;
    isSaving: boolean;

    departments: Department[];

    vendors: Vendor[];

    candidates: Candidate[];
    endDateDp: any;
    createdOnDp: any;
    modifiedOnDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: JhiDataUtils,
        private jhiAlertService: JhiAlertService,
        private bidService: BidService,
        private departmentService: DepartmentService,
        private vendorService: VendorService,
        private candidateService: CandidateService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.departmentService.query()
            .subscribe((res: HttpResponse<Department[]>) => { this.departments = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.vendorService.query()
            .subscribe((res: HttpResponse<Vendor[]>) => { this.vendors = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.candidateService.query()
            .subscribe((res: HttpResponse<Candidate[]>) => { this.candidates = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.bid.id !== undefined) {
            this.subscribeToSaveResponse(
                this.bidService.update(this.bid));
        } else {
            this.subscribeToSaveResponse(
                this.bidService.create(this.bid));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Bid>>) {
        result.subscribe((res: HttpResponse<Bid>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Bid) {
        this.eventManager.broadcast({ name: 'bidListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackDepartmentById(index: number, item: Department) {
        return item.id;
    }

    trackVendorById(index: number, item: Vendor) {
        return item.id;
    }

    trackCandidateById(index: number, item: Candidate) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}

@Component({
    selector: 'jhi-bid-popup',
    template: ''
})
export class BidPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private bidPopupService: BidPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.bidPopupService
                    .open(BidDialogComponent as Component, params['id']);
            } else {
                this.bidPopupService
                    .open(BidDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
