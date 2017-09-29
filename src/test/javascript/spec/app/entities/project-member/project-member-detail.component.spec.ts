/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { JiraliteTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ProjectMemberDetailComponent } from '../../../../../../main/webapp/app/entities/project-member/project-member-detail.component';
import { ProjectMemberService } from '../../../../../../main/webapp/app/entities/project-member/project-member.service';
import { ProjectMember } from '../../../../../../main/webapp/app/entities/project-member/project-member.model';

describe('Component Tests', () => {

    describe('ProjectMember Management Detail Component', () => {
        let comp: ProjectMemberDetailComponent;
        let fixture: ComponentFixture<ProjectMemberDetailComponent>;
        let service: ProjectMemberService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [JiraliteTestModule],
                declarations: [ProjectMemberDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ProjectMemberService,
                    JhiEventManager
                ]
            }).overrideTemplate(ProjectMemberDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ProjectMemberDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProjectMemberService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new ProjectMember(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.projectMember).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
