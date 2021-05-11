import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAppphoto } from '../appphoto.model';
import { AppphotoService } from '../service/appphoto.service';

@Component({
  templateUrl: './appphoto-delete-dialog.component.html',
})
export class AppphotoDeleteDialogComponent {
  appphoto?: IAppphoto;

  constructor(protected appphotoService: AppphotoService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.appphotoService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
