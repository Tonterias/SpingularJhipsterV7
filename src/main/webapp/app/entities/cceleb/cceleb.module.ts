import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { CcelebComponent } from './list/cceleb.component';
import { CcelebDetailComponent } from './detail/cceleb-detail.component';
import { CcelebUpdateComponent } from './update/cceleb-update.component';
import { CcelebDeleteDialogComponent } from './delete/cceleb-delete-dialog.component';
import { CcelebRoutingModule } from './route/cceleb-routing.module';

@NgModule({
  imports: [SharedModule, CcelebRoutingModule],
  declarations: [CcelebComponent, CcelebDetailComponent, CcelebUpdateComponent, CcelebDeleteDialogComponent],
  entryComponents: [CcelebDeleteDialogComponent],
})
export class CcelebModule {}
