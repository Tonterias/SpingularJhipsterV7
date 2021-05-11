jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ConfigVariablesService } from '../service/config-variables.service';
import { IConfigVariables, ConfigVariables } from '../config-variables.model';

import { ConfigVariablesUpdateComponent } from './config-variables-update.component';

describe('Component Tests', () => {
  describe('ConfigVariables Management Update Component', () => {
    let comp: ConfigVariablesUpdateComponent;
    let fixture: ComponentFixture<ConfigVariablesUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let configVariablesService: ConfigVariablesService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ConfigVariablesUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ConfigVariablesUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ConfigVariablesUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      configVariablesService = TestBed.inject(ConfigVariablesService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const configVariables: IConfigVariables = { id: 456 };

        activatedRoute.data = of({ configVariables });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(configVariables));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const configVariables = { id: 123 };
        spyOn(configVariablesService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ configVariables });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: configVariables }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(configVariablesService.update).toHaveBeenCalledWith(configVariables);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const configVariables = new ConfigVariables();
        spyOn(configVariablesService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ configVariables });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: configVariables }));
        saveSubject.complete();

        // THEN
        expect(configVariablesService.create).toHaveBeenCalledWith(configVariables);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const configVariables = { id: 123 };
        spyOn(configVariablesService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ configVariables });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(configVariablesService.update).toHaveBeenCalledWith(configVariables);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
