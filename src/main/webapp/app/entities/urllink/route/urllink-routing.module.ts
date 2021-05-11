import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { UrllinkComponent } from '../list/urllink.component';
import { UrllinkDetailComponent } from '../detail/urllink-detail.component';
import { UrllinkUpdateComponent } from '../update/urllink-update.component';
import { UrllinkRoutingResolveService } from './urllink-routing-resolve.service';

const urllinkRoute: Routes = [
  {
    path: '',
    component: UrllinkComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: UrllinkDetailComponent,
    resolve: {
      urllink: UrllinkRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: UrllinkUpdateComponent,
    resolve: {
      urllink: UrllinkRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: UrllinkUpdateComponent,
    resolve: {
      urllink: UrllinkRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(urllinkRoute)],
  exports: [RouterModule],
})
export class UrllinkRoutingModule {}
