import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BlockuserComponent } from '../list/blockuser.component';
import { BlockuserDetailComponent } from '../detail/blockuser-detail.component';
import { BlockuserUpdateComponent } from '../update/blockuser-update.component';
import { BlockuserRoutingResolveService } from './blockuser-routing-resolve.service';

const blockuserRoute: Routes = [
  {
    path: '',
    component: BlockuserComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BlockuserDetailComponent,
    resolve: {
      blockuser: BlockuserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BlockuserUpdateComponent,
    resolve: {
      blockuser: BlockuserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BlockuserUpdateComponent,
    resolve: {
      blockuser: BlockuserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(blockuserRoute)],
  exports: [RouterModule],
})
export class BlockuserRoutingModule {}
