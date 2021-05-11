import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAppuser } from '../appuser.model';
import { AppuserService } from '../service/appuser.service';

@Component({
  templateUrl: './appuser-delete-dialog.component.html',
})
export class AppuserDeleteDialogComponent {
  appuser?: IAppuser;

  constructor(protected appuserService: AppuserService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.appuserService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
