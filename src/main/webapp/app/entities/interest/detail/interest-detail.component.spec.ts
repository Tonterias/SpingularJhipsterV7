import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { InterestDetailComponent } from './interest-detail.component';

describe('Component Tests', () => {
  describe('Interest Management Detail Component', () => {
    let comp: InterestDetailComponent;
    let fixture: ComponentFixture<InterestDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [InterestDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ interest: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(InterestDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(InterestDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load interest on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.interest).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
