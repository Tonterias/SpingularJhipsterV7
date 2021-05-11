import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { UrllinkComponent } from './list/urllink.component';
import { UrllinkDetailComponent } from './detail/urllink-detail.component';
import { UrllinkUpdateComponent } from './update/urllink-update.component';
import { UrllinkDeleteDialogComponent } from './delete/urllink-delete-dialog.component';
import { UrllinkRoutingModule } from './route/urllink-routing.module';

@NgModule({
  imports: [SharedModule, UrllinkRoutingModule],
  declarations: [UrllinkComponent, UrllinkDetailComponent, UrllinkUpdateComponent, UrllinkDeleteDialogComponent],
  entryComponents: [UrllinkDeleteDialogComponent],
})
export class UrllinkModule {}
