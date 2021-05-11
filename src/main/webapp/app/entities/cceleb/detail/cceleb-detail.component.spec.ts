import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CcelebDetailComponent } from './cceleb-detail.component';

describe('Component Tests', () => {
  describe('Cceleb Management Detail Component', () => {
    let comp: CcelebDetailComponent;
    let fixture: ComponentFixture<CcelebDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [CcelebDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ cceleb: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(CcelebDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CcelebDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load cceleb on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.cceleb).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
