import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CactivityComponent } from '../list/cactivity.component';
import { CactivityDetailComponent } from '../detail/cactivity-detail.component';
import { CactivityUpdateComponent } from '../update/cactivity-update.component';
import { CactivityRoutingResolveService } from './cactivity-routing-resolve.service';

const cactivityRoute: Routes = [
  {
    path: '',
    component: CactivityComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CactivityDetailComponent,
    resolve: {
      cactivity: CactivityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CactivityUpdateComponent,
    resolve: {
      cactivity: CactivityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CactivityUpdateComponent,
    resolve: {
      cactivity: CactivityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(cactivityRoute)],
  exports: [RouterModule],
})
export class CactivityRoutingModule {}
