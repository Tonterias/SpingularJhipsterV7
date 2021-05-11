import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BlockuserDetailComponent } from './blockuser-detail.component';

describe('Component Tests', () => {
  describe('Blockuser Management Detail Component', () => {
    let comp: BlockuserDetailComponent;
    let fixture: ComponentFixture<BlockuserDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [BlockuserDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ blockuser: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(BlockuserDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(BlockuserDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load blockuser on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.blockuser).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
