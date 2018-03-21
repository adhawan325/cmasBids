/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { CmasBidsTestModule } from '../../../test.module';
import { VendorComponent } from '../../../../../../main/webapp/app/entities/vendor/vendor.component';
import { VendorService } from '../../../../../../main/webapp/app/entities/vendor/vendor.service';
import { Vendor } from '../../../../../../main/webapp/app/entities/vendor/vendor.model';

describe('Component Tests', () => {

    describe('Vendor Management Component', () => {
        let comp: VendorComponent;
        let fixture: ComponentFixture<VendorComponent>;
        let service: VendorService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [CmasBidsTestModule],
                declarations: [VendorComponent],
                providers: [
                    VendorService
                ]
            })
            .overrideTemplate(VendorComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(VendorComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(VendorService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Vendor(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.vendors[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
