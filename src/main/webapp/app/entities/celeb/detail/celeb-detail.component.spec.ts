import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CelebDetailComponent } from './celeb-detail.component';

describe('Component Tests', () => {
  describe('Celeb Management Detail Component', () => {
    let comp: CelebDetailComponent;
    let fixture: ComponentFixture<CelebDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [CelebDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ celeb: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(CelebDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CelebDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load celeb on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.celeb).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
