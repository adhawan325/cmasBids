import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { Bid } from './bid.model';
import { BidService } from './bid.service';

@Injectable()
export class BidPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private bidService: BidService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.bidService.find(id)
                    .subscribe((bidResponse: HttpResponse<Bid>) => {
                        const bid: Bid = bidResponse.body;
                        if (bid.endDate) {
                            bid.endDate = {
                                year: bid.endDate.getFullYear(),
                                month: bid.endDate.getMonth() + 1,
                                day: bid.endDate.getDate()
                            };
                        }
                        if (bid.createdOn) {
                            bid.createdOn = {
                                year: bid.createdOn.getFullYear(),
                                month: bid.createdOn.getMonth() + 1,
                                day: bid.createdOn.getDate()
                            };
                        }
                        if (bid.modifiedOn) {
                            bid.modifiedOn = {
                                year: bid.modifiedOn.getFullYear(),
                                month: bid.modifiedOn.getMonth() + 1,
                                day: bid.modifiedOn.getDate()
                            };
                        }
                        this.ngbModalRef = this.bidModalRef(component, bid);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.bidModalRef(component, new Bid());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    bidModalRef(component: Component, bid: Bid): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.bid = bid;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
