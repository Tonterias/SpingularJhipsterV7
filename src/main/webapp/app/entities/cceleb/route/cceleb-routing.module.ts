import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CcelebComponent } from '../list/cceleb.component';
import { CcelebDetailComponent } from '../detail/cceleb-detail.component';
import { CcelebUpdateComponent } from '../update/cceleb-update.component';
import { CcelebRoutingResolveService } from './cceleb-routing-resolve.service';

const ccelebRoute: Routes = [
  {
    path: '',
    component: CcelebComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CcelebDetailComponent,
    resolve: {
      cceleb: CcelebRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CcelebUpdateComponent,
    resolve: {
      cceleb: CcelebRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CcelebUpdateComponent,
    resolve: {
      cceleb: CcelebRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(ccelebRoute)],
  exports: [RouterModule],
})
export class CcelebRoutingModule {}
