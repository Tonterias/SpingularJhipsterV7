import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFeedback } from '../feedback.model';
import { FeedbackService } from '../service/feedback.service';

@Component({
  templateUrl: './feedback-delete-dialog.component.html',
})
export class FeedbackDeleteDialogComponent {
  feedback?: IFeedback;

  constructor(protected feedbackService: FeedbackService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.feedbackService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
