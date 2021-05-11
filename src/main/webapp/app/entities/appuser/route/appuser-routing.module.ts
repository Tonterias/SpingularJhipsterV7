import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AppuserComponent } from '../list/appuser.component';
import { AppuserDetailComponent } from '../detail/appuser-detail.component';
import { AppuserUpdateComponent } from '../update/appuser-update.component';
import { AppuserRoutingResolveService } from './appuser-routing-resolve.service';

const appuserRoute: Routes = [
  {
    path: '',
    component: AppuserComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AppuserDetailComponent,
    resolve: {
      appuser: AppuserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AppuserUpdateComponent,
    resolve: {
      appuser: AppuserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AppuserUpdateComponent,
    resolve: {
      appuser: AppuserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(appuserRoute)],
  exports: [RouterModule],
})
export class AppuserRoutingModule {}
