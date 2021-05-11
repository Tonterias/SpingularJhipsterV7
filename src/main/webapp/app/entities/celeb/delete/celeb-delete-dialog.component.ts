import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICeleb } from '../celeb.model';
import { CelebService } from '../service/celeb.service';

@Component({
  templateUrl: './celeb-delete-dialog.component.html',
})
export class CelebDeleteDialogComponent {
  celeb?: ICeleb;

  constructor(protected celebService: CelebService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.celebService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
