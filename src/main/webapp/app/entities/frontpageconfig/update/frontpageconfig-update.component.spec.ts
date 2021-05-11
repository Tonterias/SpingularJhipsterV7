jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { FrontpageconfigService } from '../service/frontpageconfig.service';
import { IFrontpageconfig, Frontpageconfig } from '../frontpageconfig.model';

import { FrontpageconfigUpdateComponent } from './frontpageconfig-update.component';

describe('Component Tests', () => {
  describe('Frontpageconfig Management Update Component', () => {
    let comp: FrontpageconfigUpdateComponent;
    let fixture: ComponentFixture<FrontpageconfigUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let frontpageconfigService: FrontpageconfigService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [FrontpageconfigUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(FrontpageconfigUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FrontpageconfigUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      frontpageconfigService = TestBed.inject(FrontpageconfigService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const frontpageconfig: IFrontpageconfig = { id: 456 };

        activatedRoute.data = of({ frontpageconfig });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(frontpageconfig));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const frontpageconfig = { id: 123 };
        spyOn(frontpageconfigService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ frontpageconfig });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: frontpageconfig }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(frontpageconfigService.update).toHaveBeenCalledWith(frontpageconfig);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const frontpageconfig = new Frontpageconfig();
        spyOn(frontpageconfigService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ frontpageconfig });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: frontpageconfig }));
        saveSubject.complete();

        // THEN
        expect(frontpageconfigService.create).toHaveBeenCalledWith(frontpageconfig);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const frontpageconfig = { id: 123 };
        spyOn(frontpageconfigService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ frontpageconfig });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(frontpageconfigService.update).toHaveBeenCalledWith(frontpageconfig);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
