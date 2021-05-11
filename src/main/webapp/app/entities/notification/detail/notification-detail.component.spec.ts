import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { NotificationDetailComponent } from './notification-detail.component';

describe('Component Tests', () => {
  describe('Notification Management Detail Component', () => {
    let comp: NotificationDetailComponent;
    let fixture: ComponentFixture<NotificationDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [NotificationDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ notification: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(NotificationDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(NotificationDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load notification on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.notification).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
