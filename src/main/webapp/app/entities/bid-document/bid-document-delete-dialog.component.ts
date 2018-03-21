import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { BidDocument } from './bid-document.model';
import { BidDocumentPopupService } from './bid-document-popup.service';
import { BidDocumentService } from './bid-document.service';

@Component({
    selector: 'jhi-bid-document-delete-dialog',
    templateUrl: './bid-document-delete-dialog.component.html'
})
export class BidDocumentDeleteDialogComponent {

    bidDocument: BidDocument;

    constructor(
        private bidDocumentService: BidDocumentService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.bidDocumentService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'bidDocumentListModification',
                content: 'Deleted an bidDocument'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-bid-document-delete-popup',
    template: ''
})
export class BidDocumentDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private bidDocumentPopupService: BidDocumentPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.bidDocumentPopupService
                .open(BidDocumentDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
