import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { NotificationComponent } from '../list/notification.component';
import { NotificationDetailComponent } from '../detail/notification-detail.component';
import { NotificationUpdateComponent } from '../update/notification-update.component';
import { NotificationRoutingResolveService } from './notification-routing-resolve.service';

const notificationRoute: Routes = [
  {
    path: '',
    component: NotificationComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: NotificationDetailComponent,
    resolve: {
      notification: NotificationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: NotificationUpdateComponent,
    resolve: {
      notification: NotificationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: NotificationUpdateComponent,
    resolve: {
      notification: NotificationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(notificationRoute)],
  exports: [RouterModule],
})
export class NotificationRoutingModule {}
