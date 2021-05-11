import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IUrllink } from '../urllink.model';
import { UrllinkService } from '../service/urllink.service';

@Component({
  templateUrl: './urllink-delete-dialog.component.html',
})
export class UrllinkDeleteDialogComponent {
  urllink?: IUrllink;

  constructor(protected urllinkService: UrllinkService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.urllinkService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
