import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FrontpageconfigDetailComponent } from './frontpageconfig-detail.component';

describe('Component Tests', () => {
  describe('Frontpageconfig Management Detail Component', () => {
    let comp: FrontpageconfigDetailComponent;
    let fixture: ComponentFixture<FrontpageconfigDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [FrontpageconfigDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ frontpageconfig: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(FrontpageconfigDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FrontpageconfigDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load frontpageconfig on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.frontpageconfig).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
