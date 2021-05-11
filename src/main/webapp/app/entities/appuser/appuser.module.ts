import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { AppuserComponent } from './list/appuser.component';
import { AppuserDetailComponent } from './detail/appuser-detail.component';
import { AppuserUpdateComponent } from './update/appuser-update.component';
import { AppuserDeleteDialogComponent } from './delete/appuser-delete-dialog.component';
import { AppuserRoutingModule } from './route/appuser-routing.module';

@NgModule({
  imports: [SharedModule, AppuserRoutingModule],
  declarations: [AppuserComponent, AppuserDetailComponent, AppuserUpdateComponent, AppuserDeleteDialogComponent],
  entryComponents: [AppuserDeleteDialogComponent],
})
export class AppuserModule {}
