import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ActivityComponent } from './list/activity.component';
import { ActivityDetailComponent } from './detail/activity-detail.component';
import { ActivityUpdateComponent } from './update/activity-update.component';
import { ActivityDeleteDialogComponent } from './delete/activity-delete-dialog.component';
import { ActivityRoutingModule } from './route/activity-routing.module';

@NgModule({
  imports: [SharedModule, ActivityRoutingModule],
  declarations: [ActivityComponent, ActivityDetailComponent, ActivityUpdateComponent, ActivityDeleteDialogComponent],
  entryComponents: [ActivityDeleteDialogComponent],
})
export class ActivityModule {}
