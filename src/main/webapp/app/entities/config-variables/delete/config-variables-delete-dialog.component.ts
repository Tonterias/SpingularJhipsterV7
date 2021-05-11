import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IConfigVariables } from '../config-variables.model';
import { ConfigVariablesService } from '../service/config-variables.service';

@Component({
  templateUrl: './config-variables-delete-dialog.component.html',
})
export class ConfigVariablesDeleteDialogComponent {
  configVariables?: IConfigVariables;

  constructor(protected configVariablesService: ConfigVariablesService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.configVariablesService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
