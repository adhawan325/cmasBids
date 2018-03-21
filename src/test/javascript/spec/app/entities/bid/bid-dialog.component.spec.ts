/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { CmasBidsTestModule } from '../../../test.module';
import { BidDialogComponent } from '../../../../../../main/webapp/app/entities/bid/bid-dialog.component';
import { BidService } from '../../../../../../main/webapp/app/entities/bid/bid.service';
import { Bid } from '../../../../../../main/webapp/app/entities/bid/bid.model';
import { DepartmentService } from '../../../../../../main/webapp/app/entities/department';
import { VendorService } from '../../../../../../main/webapp/app/entities/vendor';
import { CandidateService } from '../../../../../../main/webapp/app/entities/candidate';

describe('Component Tests', () => {

    describe('Bid Management Dialog Component', () => {
        let comp: BidDialogComponent;
        let fixture: ComponentFixture<BidDialogComponent>;
        let service: BidService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [CmasBidsTestModule],
                declarations: [BidDialogComponent],
                providers: [
                    DepartmentService,
                    VendorService,
                    CandidateService,
                    BidService
                ]
            })
            .overrideTemplate(BidDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(BidDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BidService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Bid(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.bid = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'bidListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Bid();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.bid = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'bidListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
