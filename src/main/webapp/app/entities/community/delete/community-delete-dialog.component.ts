import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICommunity } from '../community.model';
import { CommunityService } from '../service/community.service';

@Component({
  templateUrl: './community-delete-dialog.component.html',
})
export class CommunityDeleteDialogComponent {
  community?: ICommunity;

  constructor(protected communityService: CommunityService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.communityService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
