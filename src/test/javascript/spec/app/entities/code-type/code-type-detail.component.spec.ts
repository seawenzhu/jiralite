/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { JiraliteTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { CodeTypeDetailComponent } from '../../../../../../main/webapp/app/entities/code-type/code-type-detail.component';
import { CodeTypeService } from '../../../../../../main/webapp/app/entities/code-type/code-type.service';
import { CodeType } from '../../../../../../main/webapp/app/entities/code-type/code-type.model';

describe('Component Tests', () => {

    describe('CodeType Management Detail Component', () => {
        let comp: CodeTypeDetailComponent;
        let fixture: ComponentFixture<CodeTypeDetailComponent>;
        let service: CodeTypeService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [JiraliteTestModule],
                declarations: [CodeTypeDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    CodeTypeService,
                    JhiEventManager
                ]
            }).overrideTemplate(CodeTypeDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CodeTypeDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CodeTypeService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new CodeType(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.codeType).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
