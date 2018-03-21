/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { CmasBidsTestModule } from '../../../test.module';
import { VendorUserComponent } from '../../../../../../main/webapp/app/entities/vendor-user/vendor-user.component';
import { VendorUserService } from '../../../../../../main/webapp/app/entities/vendor-user/vendor-user.service';
import { VendorUser } from '../../../../../../main/webapp/app/entities/vendor-user/vendor-user.model';

describe('Component Tests', () => {

    describe('VendorUser Management Component', () => {
        let comp: VendorUserComponent;
        let fixture: ComponentFixture<VendorUserComponent>;
        let service: VendorUserService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [CmasBidsTestModule],
                declarations: [VendorUserComponent],
                providers: [
                    VendorUserService
                ]
            })
            .overrideTemplate(VendorUserComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(VendorUserComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(VendorUserService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new VendorUser(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.vendorUsers[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
