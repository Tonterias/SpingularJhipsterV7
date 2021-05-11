import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ConfigVariablesComponent } from './list/config-variables.component';
import { ConfigVariablesDetailComponent } from './detail/config-variables-detail.component';
import { ConfigVariablesUpdateComponent } from './update/config-variables-update.component';
import { ConfigVariablesDeleteDialogComponent } from './delete/config-variables-delete-dialog.component';
import { ConfigVariablesRoutingModule } from './route/config-variables-routing.module';

@NgModule({
  imports: [SharedModule, ConfigVariablesRoutingModule],
  declarations: [
    ConfigVariablesComponent,
    ConfigVariablesDetailComponent,
    ConfigVariablesUpdateComponent,
    ConfigVariablesDeleteDialogComponent,
  ],
  entryComponents: [ConfigVariablesDeleteDialogComponent],
})
export class ConfigVariablesModule {}
