import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FeedbackComponent } from '../list/feedback.component';
import { FeedbackDetailComponent } from '../detail/feedback-detail.component';
import { FeedbackUpdateComponent } from '../update/feedback-update.component';
import { FeedbackRoutingResolveService } from './feedback-routing-resolve.service';

const feedbackRoute: Routes = [
  {
    path: '',
    component: FeedbackComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FeedbackDetailComponent,
    resolve: {
      feedback: FeedbackRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FeedbackUpdateComponent,
    resolve: {
      feedback: FeedbackRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FeedbackUpdateComponent,
    resolve: {
      feedback: FeedbackRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(feedbackRoute)],
  exports: [RouterModule],
})
export class FeedbackRoutingModule {}
