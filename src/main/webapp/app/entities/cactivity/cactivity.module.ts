import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { CactivityComponent } from './list/cactivity.component';
import { CactivityDetailComponent } from './detail/cactivity-detail.component';
import { CactivityUpdateComponent } from './update/cactivity-update.component';
import { CactivityDeleteDialogComponent } from './delete/cactivity-delete-dialog.component';
import { CactivityRoutingModule } from './route/cactivity-routing.module';

@NgModule({
  imports: [SharedModule, CactivityRoutingModule],
  declarations: [CactivityComponent, CactivityDetailComponent, CactivityUpdateComponent, CactivityDeleteDialogComponent],
  entryComponents: [CactivityDeleteDialogComponent],
})
export class CactivityModule {}
