jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CactivityService } from '../service/cactivity.service';
import { ICactivity, Cactivity } from '../cactivity.model';
import { ICommunity } from 'app/entities/community/community.model';
import { CommunityService } from 'app/entities/community/service/community.service';

import { CactivityUpdateComponent } from './cactivity-update.component';

describe('Component Tests', () => {
  describe('Cactivity Management Update Component', () => {
    let comp: CactivityUpdateComponent;
    let fixture: ComponentFixture<CactivityUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let cactivityService: CactivityService;
    let communityService: CommunityService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CactivityUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CactivityUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CactivityUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      cactivityService = TestBed.inject(CactivityService);
      communityService = TestBed.inject(CommunityService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Community query and add missing value', () => {
        const cactivity: ICactivity = { id: 456 };
        const communities: ICommunity[] = [{ id: 50602 }];
        cactivity.communities = communities;

        const communityCollection: ICommunity[] = [{ id: 54104 }];
        spyOn(communityService, 'query').and.returnValue(of(new HttpResponse({ body: communityCollection })));
        const additionalCommunities = [...communities];
        const expectedCollection: ICommunity[] = [...additionalCommunities, ...communityCollection];
        spyOn(communityService, 'addCommunityToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ cactivity });
        comp.ngOnInit();

        expect(communityService.query).toHaveBeenCalled();
        expect(communityService.addCommunityToCollectionIfMissing).toHaveBeenCalledWith(communityCollection, ...additionalCommunities);
        expect(comp.communitiesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const cactivity: ICactivity = { id: 456 };
        const communities: ICommunity = { id: 91938 };
        cactivity.communities = [communities];

        activatedRoute.data = of({ cactivity });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(cactivity));
        expect(comp.communitiesSharedCollection).toContain(communities);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const cactivity = { id: 123 };
        spyOn(cactivityService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ cactivity });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: cactivity }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(cactivityService.update).toHaveBeenCalledWith(cactivity);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const cactivity = new Cactivity();
        spyOn(cactivityService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ cactivity });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: cactivity }));
        saveSubject.complete();

        // THEN
        expect(cactivityService.create).toHaveBeenCalledWith(cactivity);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const cactivity = { id: 123 };
        spyOn(cactivityService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ cactivity });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(cactivityService.update).toHaveBeenCalledWith(cactivity);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackCommunityById', () => {
        it('Should return tracked Community primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCommunityById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });

    describe('Getting selected relationships', () => {
      describe('getSelectedCommunity', () => {
        it('Should return option if no Community is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedCommunity(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected Community for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedCommunity(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this Community is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedCommunity(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});
