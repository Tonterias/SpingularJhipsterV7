import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ActivityDetailComponent } from './activity-detail.component';

describe('Component Tests', () => {
  describe('Activity Management Detail Component', () => {
    let comp: ActivityDetailComponent;
    let fixture: ComponentFixture<ActivityDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ActivityDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ activity: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ActivityDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ActivityDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load activity on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.activity).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
