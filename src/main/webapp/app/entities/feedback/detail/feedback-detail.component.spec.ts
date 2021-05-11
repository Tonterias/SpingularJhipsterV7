import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FeedbackDetailComponent } from './feedback-detail.component';

describe('Component Tests', () => {
  describe('Feedback Management Detail Component', () => {
    let comp: FeedbackDetailComponent;
    let fixture: ComponentFixture<FeedbackDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [FeedbackDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ feedback: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(FeedbackDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FeedbackDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load feedback on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.feedback).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
