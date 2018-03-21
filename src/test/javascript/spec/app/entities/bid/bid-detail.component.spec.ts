/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { CmasBidsTestModule } from '../../../test.module';
import { BidDetailComponent } from '../../../../../../main/webapp/app/entities/bid/bid-detail.component';
import { BidService } from '../../../../../../main/webapp/app/entities/bid/bid.service';
import { Bid } from '../../../../../../main/webapp/app/entities/bid/bid.model';

describe('Component Tests', () => {

    describe('Bid Management Detail Component', () => {
        let comp: BidDetailComponent;
        let fixture: ComponentFixture<BidDetailComponent>;
        let service: BidService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [CmasBidsTestModule],
                declarations: [BidDetailComponent],
                providers: [
                    BidService
                ]
            })
            .overrideTemplate(BidDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(BidDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BidService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Bid(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.bid).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
