import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { UrllinkDetailComponent } from './urllink-detail.component';

describe('Component Tests', () => {
  describe('Urllink Management Detail Component', () => {
    let comp: UrllinkDetailComponent;
    let fixture: ComponentFixture<UrllinkDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [UrllinkDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ urllink: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(UrllinkDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(UrllinkDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load urllink on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.urllink).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
