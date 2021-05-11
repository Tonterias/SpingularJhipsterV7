import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FrontpageconfigComponent } from '../list/frontpageconfig.component';
import { FrontpageconfigDetailComponent } from '../detail/frontpageconfig-detail.component';
import { FrontpageconfigUpdateComponent } from '../update/frontpageconfig-update.component';
import { FrontpageconfigRoutingResolveService } from './frontpageconfig-routing-resolve.service';

const frontpageconfigRoute: Routes = [
  {
    path: '',
    component: FrontpageconfigComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FrontpageconfigDetailComponent,
    resolve: {
      frontpageconfig: FrontpageconfigRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FrontpageconfigUpdateComponent,
    resolve: {
      frontpageconfig: FrontpageconfigRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FrontpageconfigUpdateComponent,
    resolve: {
      frontpageconfig: FrontpageconfigRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(frontpageconfigRoute)],
  exports: [RouterModule],
})
export class FrontpageconfigRoutingModule {}
