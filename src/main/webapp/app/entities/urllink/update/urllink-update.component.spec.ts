jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { UrllinkService } from '../service/urllink.service';
import { IUrllink, Urllink } from '../urllink.model';

import { UrllinkUpdateComponent } from './urllink-update.component';

describe('Component Tests', () => {
  describe('Urllink Management Update Component', () => {
    let comp: UrllinkUpdateComponent;
    let fixture: ComponentFixture<UrllinkUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let urllinkService: UrllinkService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [UrllinkUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(UrllinkUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(UrllinkUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      urllinkService = TestBed.inject(UrllinkService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const urllink: IUrllink = { id: 456 };

        activatedRoute.data = of({ urllink });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(urllink));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const urllink = { id: 123 };
        spyOn(urllinkService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ urllink });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: urllink }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(urllinkService.update).toHaveBeenCalledWith(urllink);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const urllink = new Urllink();
        spyOn(urllinkService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ urllink });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: urllink }));
        saveSubject.complete();

        // THEN
        expect(urllinkService.create).toHaveBeenCalledWith(urllink);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const urllink = { id: 123 };
        spyOn(urllinkService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ urllink });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(urllinkService.update).toHaveBeenCalledWith(urllink);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
