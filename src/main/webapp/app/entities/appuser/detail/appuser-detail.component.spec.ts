import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AppuserDetailComponent } from './appuser-detail.component';

describe('Component Tests', () => {
  describe('Appuser Management Detail Component', () => {
    let comp: AppuserDetailComponent;
    let fixture: ComponentFixture<AppuserDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [AppuserDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ appuser: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(AppuserDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AppuserDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load appuser on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.appuser).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
