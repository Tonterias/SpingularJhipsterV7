import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { InterestComponent } from './list/interest.component';
import { InterestDetailComponent } from './detail/interest-detail.component';
import { InterestUpdateComponent } from './update/interest-update.component';
import { InterestDeleteDialogComponent } from './delete/interest-delete-dialog.component';
import { InterestRoutingModule } from './route/interest-routing.module';

@NgModule({
  imports: [SharedModule, InterestRoutingModule],
  declarations: [InterestComponent, InterestDetailComponent, InterestUpdateComponent, InterestDeleteDialogComponent],
  entryComponents: [InterestDeleteDialogComponent],
})
export class InterestModule {}
