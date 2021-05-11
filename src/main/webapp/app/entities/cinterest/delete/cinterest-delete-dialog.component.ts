import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICinterest } from '../cinterest.model';
import { CinterestService } from '../service/cinterest.service';

@Component({
  templateUrl: './cinterest-delete-dialog.component.html',
})
export class CinterestDeleteDialogComponent {
  cinterest?: ICinterest;

  constructor(protected cinterestService: CinterestService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cinterestService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
