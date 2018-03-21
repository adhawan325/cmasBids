import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { VendorUser } from './vendor-user.model';
import { VendorUserService } from './vendor-user.service';

@Component({
    selector: 'jhi-vendor-user-detail',
    templateUrl: './vendor-user-detail.component.html'
})
export class VendorUserDetailComponent implements OnInit, OnDestroy {

    vendorUser: VendorUser;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private vendorUserService: VendorUserService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInVendorUsers();
    }

    load(id) {
        this.vendorUserService.find(id)
            .subscribe((vendorUserResponse: HttpResponse<VendorUser>) => {
                this.vendorUser = vendorUserResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInVendorUsers() {
        this.eventSubscriber = this.eventManager.subscribe(
            'vendorUserListModification',
            (response) => this.load(this.vendorUser.id)
        );
    }
}
