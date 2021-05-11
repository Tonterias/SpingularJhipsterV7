import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ConfigVariablesComponent } from '../list/config-variables.component';
import { ConfigVariablesDetailComponent } from '../detail/config-variables-detail.component';
import { ConfigVariablesUpdateComponent } from '../update/config-variables-update.component';
import { ConfigVariablesRoutingResolveService } from './config-variables-routing-resolve.service';

const configVariablesRoute: Routes = [
  {
    path: '',
    component: ConfigVariablesComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ConfigVariablesDetailComponent,
    resolve: {
      configVariables: ConfigVariablesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ConfigVariablesUpdateComponent,
    resolve: {
      configVariables: ConfigVariablesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ConfigVariablesUpdateComponent,
    resolve: {
      configVariables: ConfigVariablesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(configVariablesRoute)],
  exports: [RouterModule],
})
export class ConfigVariablesRoutingModule {}
