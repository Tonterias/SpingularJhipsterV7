import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IComment } from '../comment.model';
import { CommentService } from '../service/comment.service';

@Component({
  templateUrl: './comment-delete-dialog.component.html',
})
export class CommentDeleteDialogComponent {
  comment?: IComment;

  constructor(protected commentService: CommentService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.commentService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
