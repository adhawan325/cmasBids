/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { CmasBidsTestModule } from '../../../test.module';
import { VendorUserDetailComponent } from '../../../../../../main/webapp/app/entities/vendor-user/vendor-user-detail.component';
import { VendorUserService } from '../../../../../../main/webapp/app/entities/vendor-user/vendor-user.service';
import { VendorUser } from '../../../../../../main/webapp/app/entities/vendor-user/vendor-user.model';

describe('Component Tests', () => {

    describe('VendorUser Management Detail Component', () => {
        let comp: VendorUserDetailComponent;
        let fixture: ComponentFixture<VendorUserDetailComponent>;
        let service: VendorUserService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [CmasBidsTestModule],
                declarations: [VendorUserDetailComponent],
                providers: [
                    VendorUserService
                ]
            })
            .overrideTemplate(VendorUserDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(VendorUserDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(VendorUserService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new VendorUser(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.vendorUser).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
