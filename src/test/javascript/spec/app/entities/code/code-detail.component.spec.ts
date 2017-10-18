/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { JiraliteTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { CodeDetailComponent } from '../../../../../../main/webapp/app/entities/code/code-detail.component';
import { CodeService } from '../../../../../../main/webapp/app/entities/code/code.service';
import { Code } from '../../../../../../main/webapp/app/entities/code/code.model';

describe('Component Tests', () => {

    describe('Code Management Detail Component', () => {
        let comp: CodeDetailComponent;
        let fixture: ComponentFixture<CodeDetailComponent>;
        let service: CodeService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [JiraliteTestModule],
                declarations: [CodeDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    CodeService,
                    JhiEventManager
                ]
            }).overrideTemplate(CodeDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CodeDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CodeService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Code(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.code).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
