import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBlockuser } from '../blockuser.model';
import { BlockuserService } from '../service/blockuser.service';

@Component({
  templateUrl: './blockuser-delete-dialog.component.html',
})
export class BlockuserDeleteDialogComponent {
  blockuser?: IBlockuser;

  constructor(protected blockuserService: BlockuserService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.blockuserService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
