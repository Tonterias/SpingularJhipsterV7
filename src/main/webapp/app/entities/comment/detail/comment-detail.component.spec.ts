import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CommentDetailComponent } from './comment-detail.component';

describe('Component Tests', () => {
  describe('Comment Management Detail Component', () => {
    let comp: CommentDetailComponent;
    let fixture: ComponentFixture<CommentDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [CommentDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ comment: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(CommentDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CommentDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load comment on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.comment).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
