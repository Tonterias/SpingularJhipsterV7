import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CelebComponent } from '../list/celeb.component';
import { CelebDetailComponent } from '../detail/celeb-detail.component';
import { CelebUpdateComponent } from '../update/celeb-update.component';
import { CelebRoutingResolveService } from './celeb-routing-resolve.service';

const celebRoute: Routes = [
  {
    path: '',
    component: CelebComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CelebDetailComponent,
    resolve: {
      celeb: CelebRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CelebUpdateComponent,
    resolve: {
      celeb: CelebRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CelebUpdateComponent,
    resolve: {
      celeb: CelebRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(celebRoute)],
  exports: [RouterModule],
})
export class CelebRoutingModule {}
