import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CactivityDetailComponent } from './cactivity-detail.component';

describe('Component Tests', () => {
  describe('Cactivity Management Detail Component', () => {
    let comp: CactivityDetailComponent;
    let fixture: ComponentFixture<CactivityDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [CactivityDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ cactivity: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(CactivityDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CactivityDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load cactivity on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.cactivity).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
