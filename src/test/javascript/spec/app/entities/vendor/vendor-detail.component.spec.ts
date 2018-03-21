/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { CmasBidsTestModule } from '../../../test.module';
import { VendorDetailComponent } from '../../../../../../main/webapp/app/entities/vendor/vendor-detail.component';
import { VendorService } from '../../../../../../main/webapp/app/entities/vendor/vendor.service';
import { Vendor } from '../../../../../../main/webapp/app/entities/vendor/vendor.model';

describe('Component Tests', () => {

    describe('Vendor Management Detail Component', () => {
        let comp: VendorDetailComponent;
        let fixture: ComponentFixture<VendorDetailComponent>;
        let service: VendorService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [CmasBidsTestModule],
                declarations: [VendorDetailComponent],
                providers: [
                    VendorService
                ]
            })
            .overrideTemplate(VendorDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(VendorDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(VendorService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Vendor(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.vendor).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
