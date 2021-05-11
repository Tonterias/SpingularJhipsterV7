jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { BlockuserService } from '../service/blockuser.service';

import { BlockuserDeleteDialogComponent } from './blockuser-delete-dialog.component';

describe('Component Tests', () => {
  describe('Blockuser Management Delete Component', () => {
    let comp: BlockuserDeleteDialogComponent;
    let fixture: ComponentFixture<BlockuserDeleteDialogComponent>;
    let service: BlockuserService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [BlockuserDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(BlockuserDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(BlockuserDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(BlockuserService);
      mockActiveModal = TestBed.inject(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.close).not.toHaveBeenCalled();
        expect(mockActiveModal.dismiss).toHaveBeenCalled();
      });
    });
  });
});
