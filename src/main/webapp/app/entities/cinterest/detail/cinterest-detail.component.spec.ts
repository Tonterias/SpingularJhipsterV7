import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CinterestDetailComponent } from './cinterest-detail.component';

describe('Component Tests', () => {
  describe('Cinterest Management Detail Component', () => {
    let comp: CinterestDetailComponent;
    let fixture: ComponentFixture<CinterestDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [CinterestDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ cinterest: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(CinterestDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CinterestDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load cinterest on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.cinterest).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
