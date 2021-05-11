import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { CinterestComponent } from './list/cinterest.component';
import { CinterestDetailComponent } from './detail/cinterest-detail.component';
import { CinterestUpdateComponent } from './update/cinterest-update.component';
import { CinterestDeleteDialogComponent } from './delete/cinterest-delete-dialog.component';
import { CinterestRoutingModule } from './route/cinterest-routing.module';

@NgModule({
  imports: [SharedModule, CinterestRoutingModule],
  declarations: [CinterestComponent, CinterestDetailComponent, CinterestUpdateComponent, CinterestDeleteDialogComponent],
  entryComponents: [CinterestDeleteDialogComponent],
})
export class CinterestModule {}
