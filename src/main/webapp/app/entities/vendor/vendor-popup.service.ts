import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { Vendor } from './vendor.model';
import { VendorService } from './vendor.service';

@Injectable()
export class VendorPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private vendorService: VendorService

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
                this.vendorService.find(id)
                    .subscribe((vendorResponse: HttpResponse<Vendor>) => {
                        const vendor: Vendor = vendorResponse.body;
                        if (vendor.vendorEndDate) {
                            vendor.vendorEndDate = {
                                year: vendor.vendorEndDate.getFullYear(),
                                month: vendor.vendorEndDate.getMonth() + 1,
                                day: vendor.vendorEndDate.getDate()
                            };
                        }
                        this.ngbModalRef = this.vendorModalRef(component, vendor);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.vendorModalRef(component, new Vendor());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    vendorModalRef(component: Component, vendor: Vendor): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.vendor = vendor;
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
