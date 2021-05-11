import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFollow } from '../follow.model';
import { FollowService } from '../service/follow.service';

@Component({
  templateUrl: './follow-delete-dialog.component.html',
})
export class FollowDeleteDialogComponent {
  follow?: IFollow;

  constructor(protected followService: FollowService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.followService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
