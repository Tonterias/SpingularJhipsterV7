import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICceleb } from '../cceleb.model';
import { CcelebService } from '../service/cceleb.service';

@Component({
  templateUrl: './cceleb-delete-dialog.component.html',
})
export class CcelebDeleteDialogComponent {
  cceleb?: ICceleb;

  constructor(protected ccelebService: CcelebService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ccelebService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
