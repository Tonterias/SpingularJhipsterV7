import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AppphotoComponent } from '../list/appphoto.component';
import { AppphotoDetailComponent } from '../detail/appphoto-detail.component';
import { AppphotoUpdateComponent } from '../update/appphoto-update.component';
import { AppphotoRoutingResolveService } from './appphoto-routing-resolve.service';

const appphotoRoute: Routes = [
  {
    path: '',
    component: AppphotoComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AppphotoDetailComponent,
    resolve: {
      appphoto: AppphotoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AppphotoUpdateComponent,
    resolve: {
      appphoto: AppphotoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AppphotoUpdateComponent,
    resolve: {
      appphoto: AppphotoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(appphotoRoute)],
  exports: [RouterModule],
})
export class AppphotoRoutingModule {}
