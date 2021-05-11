import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IInterest } from '../interest.model';
import { InterestService } from '../service/interest.service';

@Component({
  templateUrl: './interest-delete-dialog.component.html',
})
export class InterestDeleteDialogComponent {
  interest?: IInterest;

  constructor(protected interestService: InterestService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.interestService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
