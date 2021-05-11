import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { CommentComponent } from './list/comment.component';
import { CommentDetailComponent } from './detail/comment-detail.component';
import { CommentUpdateComponent } from './update/comment-update.component';
import { CommentDeleteDialogComponent } from './delete/comment-delete-dialog.component';
import { CommentRoutingModule } from './route/comment-routing.module';

@NgModule({
  imports: [SharedModule, CommentRoutingModule],
  declarations: [CommentComponent, CommentDetailComponent, CommentUpdateComponent, CommentDeleteDialogComponent],
  entryComponents: [CommentDeleteDialogComponent],
})
export class CommentModule {}
