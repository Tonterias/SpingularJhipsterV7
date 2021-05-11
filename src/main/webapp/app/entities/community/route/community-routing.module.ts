import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CommunityComponent } from '../list/community.component';
import { CommunityDetailComponent } from '../detail/community-detail.component';
import { CommunityUpdateComponent } from '../update/community-update.component';
import { CommunityRoutingResolveService } from './community-routing-resolve.service';

const communityRoute: Routes = [
  {
    path: '',
    component: CommunityComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CommunityDetailComponent,
    resolve: {
      community: CommunityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CommunityUpdateComponent,
    resolve: {
      community: CommunityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CommunityUpdateComponent,
    resolve: {
      community: CommunityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(communityRoute)],
  exports: [RouterModule],
})
export class CommunityRoutingModule {}
