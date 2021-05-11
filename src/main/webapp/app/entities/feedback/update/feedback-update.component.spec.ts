jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { FeedbackService } from '../service/feedback.service';
import { IFeedback, Feedback } from '../feedback.model';

import { FeedbackUpdateComponent } from './feedback-update.component';

describe('Component Tests', () => {
  describe('Feedback Management Update Component', () => {
    let comp: FeedbackUpdateComponent;
    let fixture: ComponentFixture<FeedbackUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let feedbackService: FeedbackService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [FeedbackUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(FeedbackUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FeedbackUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      feedbackService = TestBed.inject(FeedbackService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const feedback: IFeedback = { id: 456 };

        activatedRoute.data = of({ feedback });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(feedback));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const feedback = { id: 123 };
        spyOn(feedbackService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ feedback });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: feedback }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(feedbackService.update).toHaveBeenCalledWith(feedback);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const feedback = new Feedback();
        spyOn(feedbackService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ feedback });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: feedback }));
        saveSubject.complete();

        // THEN
        expect(feedbackService.create).toHaveBeenCalledWith(feedback);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const feedback = { id: 123 };
        spyOn(feedbackService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ feedback });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(feedbackService.update).toHaveBeenCalledWith(feedback);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
