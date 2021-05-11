import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { FrontpageconfigComponent } from './list/frontpageconfig.component';
import { FrontpageconfigDetailComponent } from './detail/frontpageconfig-detail.component';
import { FrontpageconfigUpdateComponent } from './update/frontpageconfig-update.component';
import { FrontpageconfigDeleteDialogComponent } from './delete/frontpageconfig-delete-dialog.component';
import { FrontpageconfigRoutingModule } from './route/frontpageconfig-routing.module';

@NgModule({
  imports: [SharedModule, FrontpageconfigRoutingModule],
  declarations: [
    FrontpageconfigComponent,
    FrontpageconfigDetailComponent,
    FrontpageconfigUpdateComponent,
    FrontpageconfigDeleteDialogComponent,
  ],
  entryComponents: [FrontpageconfigDeleteDialogComponent],
})
export class FrontpageconfigModule {}
