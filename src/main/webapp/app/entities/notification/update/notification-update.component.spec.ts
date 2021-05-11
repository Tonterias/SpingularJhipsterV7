jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { NotificationService } from '../service/notification.service';
import { INotification, Notification } from '../notification.model';
import { IAppuser } from 'app/entities/appuser/appuser.model';
import { AppuserService } from 'app/entities/appuser/service/appuser.service';

import { NotificationUpdateComponent } from './notification-update.component';

describe('Component Tests', () => {
  describe('Notification Management Update Component', () => {
    let comp: NotificationUpdateComponent;
    let fixture: ComponentFixture<NotificationUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let notificationService: NotificationService;
    let appuserService: AppuserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [NotificationUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(NotificationUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(NotificationUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      notificationService = TestBed.inject(NotificationService);
      appuserService = TestBed.inject(AppuserService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Appuser query and add missing value', () => {
        const notification: INotification = { id: 456 };
        const appuser: IAppuser = { id: 14985 };
        notification.appuser = appuser;

        const appuserCollection: IAppuser[] = [{ id: 66276 }];
        spyOn(appuserService, 'query').and.returnValue(of(new HttpResponse({ body: appuserCollection })));
        const additionalAppusers = [appuser];
        const expectedCollection: IAppuser[] = [...additionalAppusers, ...appuserCollection];
        spyOn(appuserService, 'addAppuserToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ notification });
        comp.ngOnInit();

        expect(appuserService.query).toHaveBeenCalled();
        expect(appuserService.addAppuserToCollectionIfMissing).toHaveBeenCalledWith(appuserCollection, ...additionalAppusers);
        expect(comp.appusersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const notification: INotification = { id: 456 };
        const appuser: IAppuser = { id: 86447 };
        notification.appuser = appuser;

        activatedRoute.data = of({ notification });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(notification));
        expect(comp.appusersSharedCollection).toContain(appuser);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const notification = { id: 123 };
        spyOn(notificationService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ notification });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: notification }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(notificationService.update).toHaveBeenCalledWith(notification);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const notification = new Notification();
        spyOn(notificationService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ notification });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: notification }));
        saveSubject.complete();

        // THEN
        expect(notificationService.create).toHaveBeenCalledWith(notification);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const notification = { id: 123 };
        spyOn(notificationService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ notification });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(notificationService.update).toHaveBeenCalledWith(notification);
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
  });
});
