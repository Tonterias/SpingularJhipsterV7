import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { InterestComponent } from '../list/interest.component';
import { InterestDetailComponent } from '../detail/interest-detail.component';
import { InterestUpdateComponent } from '../update/interest-update.component';
import { InterestRoutingResolveService } from './interest-routing-resolve.service';

const interestRoute: Routes = [
  {
    path: '',
    component: InterestComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: InterestDetailComponent,
    resolve: {
      interest: InterestRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: InterestUpdateComponent,
    resolve: {
      interest: InterestRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: InterestUpdateComponent,
    resolve: {
      interest: InterestRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(interestRoute)],
  exports: [RouterModule],
})
export class InterestRoutingModule {}
