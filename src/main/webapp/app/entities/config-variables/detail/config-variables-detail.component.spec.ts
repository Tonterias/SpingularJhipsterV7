import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ConfigVariablesDetailComponent } from './config-variables-detail.component';

describe('Component Tests', () => {
  describe('ConfigVariables Management Detail Component', () => {
    let comp: ConfigVariablesDetailComponent;
    let fixture: ComponentFixture<ConfigVariablesDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ConfigVariablesDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ configVariables: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ConfigVariablesDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ConfigVariablesDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load configVariables on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.configVariables).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
