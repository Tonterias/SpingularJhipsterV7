import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ActivityComponent } from '../list/activity.component';
import { ActivityDetailComponent } from '../detail/activity-detail.component';
import { ActivityUpdateComponent } from '../update/activity-update.component';
import { ActivityRoutingResolveService } from './activity-routing-resolve.service';

const activityRoute: Routes = [
  {
    path: '',
    component: ActivityComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ActivityDetailComponent,
    resolve: {
      activity: ActivityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ActivityUpdateComponent,
    resolve: {
      activity: ActivityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ActivityUpdateComponent,
    resolve: {
      activity: ActivityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(activityRoute)],
  exports: [RouterModule],
})
export class ActivityRoutingModule {}
