import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { BlockuserComponent } from './list/blockuser.component';
import { BlockuserDetailComponent } from './detail/blockuser-detail.component';
import { BlockuserUpdateComponent } from './update/blockuser-update.component';
import { BlockuserDeleteDialogComponent } from './delete/blockuser-delete-dialog.component';
import { BlockuserRoutingModule } from './route/blockuser-routing.module';

@NgModule({
  imports: [SharedModule, BlockuserRoutingModule],
  declarations: [BlockuserComponent, BlockuserDetailComponent, BlockuserUpdateComponent, BlockuserDeleteDialogComponent],
  entryComponents: [BlockuserDeleteDialogComponent],
})
export class BlockuserModule {}
