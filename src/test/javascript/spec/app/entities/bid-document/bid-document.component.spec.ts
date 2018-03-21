/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { CmasBidsTestModule } from '../../../test.module';
import { BidDocumentComponent } from '../../../../../../main/webapp/app/entities/bid-document/bid-document.component';
import { BidDocumentService } from '../../../../../../main/webapp/app/entities/bid-document/bid-document.service';
import { BidDocument } from '../../../../../../main/webapp/app/entities/bid-document/bid-document.model';

describe('Component Tests', () => {

    describe('BidDocument Management Component', () => {
        let comp: BidDocumentComponent;
        let fixture: ComponentFixture<BidDocumentComponent>;
        let service: BidDocumentService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [CmasBidsTestModule],
                declarations: [BidDocumentComponent],
                providers: [
                    BidDocumentService
                ]
            })
            .overrideTemplate(BidDocumentComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(BidDocumentComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BidDocumentService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new BidDocument(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.bidDocuments[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
