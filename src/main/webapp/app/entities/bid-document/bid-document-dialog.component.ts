import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { BidDocument } from './bid-document.model';
import { BidDocumentPopupService } from './bid-document-popup.service';
import { BidDocumentService } from './bid-document.service';
import { Bid, BidService } from '../bid';

@Component({
    selector: 'jhi-bid-document-dialog',
    templateUrl: './bid-document-dialog.component.html'
})
export class BidDocumentDialogComponent implements OnInit {

    bidDocument: BidDocument;
    isSaving: boolean;

    bids: Bid[];

    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: JhiDataUtils,
        private jhiAlertService: JhiAlertService,
        private bidDocumentService: BidDocumentService,
        private bidService: BidService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.bidService.query()
            .subscribe((res: HttpResponse<Bid[]>) => { this.bids = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
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
        if (this.bidDocument.id !== undefined) {
            this.subscribeToSaveResponse(
                this.bidDocumentService.update(this.bidDocument));
        } else {
            this.subscribeToSaveResponse(
                this.bidDocumentService.create(this.bidDocument));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<BidDocument>>) {
        result.subscribe((res: HttpResponse<BidDocument>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: BidDocument) {
        this.eventManager.broadcast({ name: 'bidDocumentListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackBidById(index: number, item: Bid) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-bid-document-popup',
    template: ''
})
export class BidDocumentPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private bidDocumentPopupService: BidDocumentPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.bidDocumentPopupService
                    .open(BidDocumentDialogComponent as Component, params['id']);
            } else {
                this.bidDocumentPopupService
                    .open(BidDocumentDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
