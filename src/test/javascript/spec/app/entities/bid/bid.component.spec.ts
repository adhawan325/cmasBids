/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { CmasBidsTestModule } from '../../../test.module';
import { BidComponent } from '../../../../../../main/webapp/app/entities/bid/bid.component';
import { BidService } from '../../../../../../main/webapp/app/entities/bid/bid.service';
import { Bid } from '../../../../../../main/webapp/app/entities/bid/bid.model';

describe('Component Tests', () => {

    describe('Bid Management Component', () => {
        let comp: BidComponent;
        let fixture: ComponentFixture<BidComponent>;
        let service: BidService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [CmasBidsTestModule],
                declarations: [BidComponent],
                providers: [
                    BidService
                ]
            })
            .overrideTemplate(BidComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(BidComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BidService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Bid(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.bids[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
