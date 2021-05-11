import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CinterestComponent } from '../list/cinterest.component';
import { CinterestDetailComponent } from '../detail/cinterest-detail.component';
import { CinterestUpdateComponent } from '../update/cinterest-update.component';
import { CinterestRoutingResolveService } from './cinterest-routing-resolve.service';

const cinterestRoute: Routes = [
  {
    path: '',
    component: CinterestComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CinterestDetailComponent,
    resolve: {
      cinterest: CinterestRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CinterestUpdateComponent,
    resolve: {
      cinterest: CinterestRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CinterestUpdateComponent,
    resolve: {
      cinterest: CinterestRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(cinterestRoute)],
  exports: [RouterModule],
})
export class CinterestRoutingModule {}
