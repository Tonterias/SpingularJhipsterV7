import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CommentComponent } from '../list/comment.component';
import { CommentDetailComponent } from '../detail/comment-detail.component';
import { CommentUpdateComponent } from '../update/comment-update.component';
import { CommentRoutingResolveService } from './comment-routing-resolve.service';

const commentRoute: Routes = [
  {
    path: '',
    component: CommentComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CommentDetailComponent,
    resolve: {
      comment: CommentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CommentUpdateComponent,
    resolve: {
      comment: CommentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CommentUpdateComponent,
    resolve: {
      comment: CommentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(commentRoute)],
  exports: [RouterModule],
})
export class CommentRoutingModule {}
