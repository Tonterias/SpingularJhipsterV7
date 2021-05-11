import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFrontpageconfig } from '../frontpageconfig.model';
import { FrontpageconfigService } from '../service/frontpageconfig.service';

@Component({
  templateUrl: './frontpageconfig-delete-dialog.component.html',
})
export class FrontpageconfigDeleteDialogComponent {
  frontpageconfig?: IFrontpageconfig;

  constructor(protected frontpageconfigService: FrontpageconfigService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.frontpageconfigService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
