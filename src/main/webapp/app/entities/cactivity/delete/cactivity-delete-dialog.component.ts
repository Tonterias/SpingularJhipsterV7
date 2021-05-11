import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICactivity } from '../cactivity.model';
import { CactivityService } from '../service/cactivity.service';

@Component({
  templateUrl: './cactivity-delete-dialog.component.html',
})
export class CactivityDeleteDialogComponent {
  cactivity?: ICactivity;

  constructor(protected cactivityService: CactivityService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cactivityService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
