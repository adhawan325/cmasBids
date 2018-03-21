/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { CmasBidsTestModule } from '../../../test.module';
import { BidDocumentDetailComponent } from '../../../../../../main/webapp/app/entities/bid-document/bid-document-detail.component';
import { BidDocumentService } from '../../../../../../main/webapp/app/entities/bid-document/bid-document.service';
import { BidDocument } from '../../../../../../main/webapp/app/entities/bid-document/bid-document.model';

describe('Component Tests', () => {

    describe('BidDocument Management Detail Component', () => {
        let comp: BidDocumentDetailComponent;
        let fixture: ComponentFixture<BidDocumentDetailComponent>;
        let service: BidDocumentService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [CmasBidsTestModule],
                declarations: [BidDocumentDetailComponent],
                providers: [
                    BidDocumentService
                ]
            })
            .overrideTemplate(BidDocumentDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(BidDocumentDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BidDocumentService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new BidDocument(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.bidDocument).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
