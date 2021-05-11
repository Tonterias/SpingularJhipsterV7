jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AppphotoService } from '../service/appphoto.service';
import { IAppphoto, Appphoto } from '../appphoto.model';
import { IAppuser } from 'app/entities/appuser/appuser.model';
import { AppuserService } from 'app/entities/appuser/service/appuser.service';

import { AppphotoUpdateComponent } from './appphoto-update.component';

describe('Component Tests', () => {
  describe('Appphoto Management Update Component', () => {
    let comp: AppphotoUpdateComponent;
    let fixture: ComponentFixture<AppphotoUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let appphotoService: AppphotoService;
    let appuserService: AppuserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AppphotoUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(AppphotoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AppphotoUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      appphotoService = TestBed.inject(AppphotoService);
      appuserService = TestBed.inject(AppuserService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call appuser query and add missing value', () => {
        const appphoto: IAppphoto = { id: 456 };
        const appuser: IAppuser = { id: 32207 };
        appphoto.appuser = appuser;

        const appuserCollection: IAppuser[] = [{ id: 3998 }];
        spyOn(appuserService, 'query').and.returnValue(of(new HttpResponse({ body: appuserCollection })));
        const expectedCollection: IAppuser[] = [appuser, ...appuserCollection];
        spyOn(appuserService, 'addAppuserToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ appphoto });
        comp.ngOnInit();

        expect(appuserService.query).toHaveBeenCalled();
        expect(appuserService.addAppuserToCollectionIfMissing).toHaveBeenCalledWith(appuserCollection, appuser);
        expect(comp.appusersCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const appphoto: IAppphoto = { id: 456 };
        const appuser: IAppuser = { id: 51227 };
        appphoto.appuser = appuser;

        activatedRoute.data = of({ appphoto });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(appphoto));
        expect(comp.appusersCollection).toContain(appuser);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const appphoto = { id: 123 };
        spyOn(appphotoService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ appphoto });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: appphoto }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(appphotoService.update).toHaveBeenCalledWith(appphoto);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const appphoto = new Appphoto();
        spyOn(appphotoService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ appphoto });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: appphoto }));
        saveSubject.complete();

        // THEN
        expect(appphotoService.create).toHaveBeenCalledWith(appphoto);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const appphoto = { id: 123 };
        spyOn(appphotoService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ appphoto });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(appphotoService.update).toHaveBeenCalledWith(appphoto);
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
