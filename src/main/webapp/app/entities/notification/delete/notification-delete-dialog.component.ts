import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { INotification } from '../notification.model';
import { NotificationService } from '../service/notification.service';

@Component({
  templateUrl: './notification-delete-dialog.component.html',
})
export class NotificationDeleteDialogComponent {
  notification?: INotification;

  constructor(protected notificationService: NotificationService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.notificationService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
