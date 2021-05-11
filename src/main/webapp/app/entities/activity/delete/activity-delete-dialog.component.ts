import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IActivity } from '../activity.model';
import { ActivityService } from '../service/activity.service';

@Component({
  templateUrl: './activity-delete-dialog.component.html',
})
export class ActivityDeleteDialogComponent {
  activity?: IActivity;

  constructor(protected activityService: ActivityService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.activityService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
