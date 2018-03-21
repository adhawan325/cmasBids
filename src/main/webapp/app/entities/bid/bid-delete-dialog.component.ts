import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Bid } from './bid.model';
import { BidPopupService } from './bid-popup.service';
import { BidService } from './bid.service';

@Component({
    selector: 'jhi-bid-delete-dialog',
    templateUrl: './bid-delete-dialog.component.html'
})
export class BidDeleteDialogComponent {

    bid: Bid;

    constructor(
        private bidService: BidService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.bidService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'bidListModification',
                content: 'Deleted an bid'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-bid-delete-popup',
    template: ''
})
export class BidDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private bidPopupService: BidPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.bidPopupService
                .open(BidDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
