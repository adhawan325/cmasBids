import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';

import { BidDocument } from './bid-document.model';
import { BidDocumentService } from './bid-document.service';

@Component({
    selector: 'jhi-bid-document-detail',
    templateUrl: './bid-document-detail.component.html'
})
export class BidDocumentDetailComponent implements OnInit, OnDestroy {

    bidDocument: BidDocument;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private dataUtils: JhiDataUtils,
        private bidDocumentService: BidDocumentService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInBidDocuments();
    }

    load(id) {
        this.bidDocumentService.find(id)
            .subscribe((bidDocumentResponse: HttpResponse<BidDocument>) => {
                this.bidDocument = bidDocumentResponse.body;
            });
    }
    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInBidDocuments() {
        this.eventSubscriber = this.eventManager.subscribe(
            'bidDocumentListModification',
            (response) => this.load(this.bidDocument.id)
        );
    }
}
