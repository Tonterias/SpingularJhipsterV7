jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CinterestService } from '../service/cinterest.service';
import { ICinterest, Cinterest } from '../cinterest.model';
import { ICommunity } from 'app/entities/community/community.model';
import { CommunityService } from 'app/entities/community/service/community.service';

import { CinterestUpdateComponent } from './cinterest-update.component';

describe('Component Tests', () => {
  describe('Cinterest Management Update Component', () => {
    let comp: CinterestUpdateComponent;
    let fixture: ComponentFixture<CinterestUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let cinterestService: CinterestService;
    let communityService: CommunityService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CinterestUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CinterestUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CinterestUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      cinterestService = TestBed.inject(CinterestService);
      communityService = TestBed.inject(CommunityService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Community query and add missing value', () => {
        const cinterest: ICinterest = { id: 456 };
        const communities: ICommunity[] = [{ id: 72417 }];
        cinterest.communities = communities;

        const communityCollection: ICommunity[] = [{ id: 71362 }];
        spyOn(communityService, 'query').and.returnValue(of(new HttpResponse({ body: communityCollection })));
        const additionalCommunities = [...communities];
        const expectedCollection: ICommunity[] = [...additionalCommunities, ...communityCollection];
        spyOn(communityService, 'addCommunityToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ cinterest });
        comp.ngOnInit();

        expect(communityService.query).toHaveBeenCalled();
        expect(communityService.addCommunityToCollectionIfMissing).toHaveBeenCalledWith(communityCollection, ...additionalCommunities);
        expect(comp.communitiesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const cinterest: ICinterest = { id: 456 };
        const communities: ICommunity = { id: 22178 };
        cinterest.communities = [communities];

        activatedRoute.data = of({ cinterest });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(cinterest));
        expect(comp.communitiesSharedCollection).toContain(communities);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const cinterest = { id: 123 };
        spyOn(cinterestService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ cinterest });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: cinterest }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(cinterestService.update).toHaveBeenCalledWith(cinterest);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const cinterest = new Cinterest();
        spyOn(cinterestService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ cinterest });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: cinterest }));
        saveSubject.complete();

        // THEN
        expect(cinterestService.create).toHaveBeenCalledWith(cinterest);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const cinterest = { id: 123 };
        spyOn(cinterestService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ cinterest });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(cinterestService.update).toHaveBeenCalledWith(cinterest);
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
