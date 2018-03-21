import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';

import { Bid } from './bid.model';
import { BidService } from './bid.service';

@Component({
    selector: 'jhi-bid-detail',
    templateUrl: './bid-detail.component.html'
})
export class BidDetailComponent implements OnInit, OnDestroy {

    bid: Bid;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private dataUtils: JhiDataUtils,
        private bidService: BidService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInBids();
    }

    load(id) {
        this.bidService.find(id)
            .subscribe((bidResponse: HttpResponse<Bid>) => {
                this.bid = bidResponse.body;
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

    registerChangeInBids() {
        this.eventSubscriber = this.eventManager.subscribe(
            'bidListModification',
            (response) => this.load(this.bid.id)
        );
    }
}
