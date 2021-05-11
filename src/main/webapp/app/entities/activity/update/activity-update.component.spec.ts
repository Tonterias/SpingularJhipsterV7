jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ActivityService } from '../service/activity.service';
import { IActivity, Activity } from '../activity.model';
import { IAppuser } from 'app/entities/appuser/appuser.model';
import { AppuserService } from 'app/entities/appuser/service/appuser.service';

import { ActivityUpdateComponent } from './activity-update.component';

describe('Component Tests', () => {
  describe('Activity Management Update Component', () => {
    let comp: ActivityUpdateComponent;
    let fixture: ComponentFixture<ActivityUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let activityService: ActivityService;
    let appuserService: AppuserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ActivityUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ActivityUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ActivityUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      activityService = TestBed.inject(ActivityService);
      appuserService = TestBed.inject(AppuserService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Appuser query and add missing value', () => {
        const activity: IActivity = { id: 456 };
        const appusers: IAppuser[] = [{ id: 61900 }];
        activity.appusers = appusers;

        const appuserCollection: IAppuser[] = [{ id: 60485 }];
        spyOn(appuserService, 'query').and.returnValue(of(new HttpResponse({ body: appuserCollection })));
        const additionalAppusers = [...appusers];
        const expectedCollection: IAppuser[] = [...additionalAppusers, ...appuserCollection];
        spyOn(appuserService, 'addAppuserToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ activity });
        comp.ngOnInit();

        expect(appuserService.query).toHaveBeenCalled();
        expect(appuserService.addAppuserToCollectionIfMissing).toHaveBeenCalledWith(appuserCollection, ...additionalAppusers);
        expect(comp.appusersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const activity: IActivity = { id: 456 };
        const appusers: IAppuser = { id: 51460 };
        activity.appusers = [appusers];

        activatedRoute.data = of({ activity });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(activity));
        expect(comp.appusersSharedCollection).toContain(appusers);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const activity = { id: 123 };
        spyOn(activityService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ activity });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: activity }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(activityService.update).toHaveBeenCalledWith(activity);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const activity = new Activity();
        spyOn(activityService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ activity });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: activity }));
        saveSubject.complete();

        // THEN
        expect(activityService.create).toHaveBeenCalledWith(activity);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const activity = { id: 123 };
        spyOn(activityService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ activity });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(activityService.update).toHaveBeenCalledWith(activity);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackAppuserById', () => {
        it('Should return tracked Appuser primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackAppuserById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });

    describe('Getting selected relationships', () => {
      describe('getSelectedAppuser', () => {
        it('Should return option if no Appuser is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedAppuser(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected Appuser for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedAppuser(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this Appuser is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedAppuser(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});
