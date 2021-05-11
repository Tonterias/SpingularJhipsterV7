import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { AppphotoComponent } from './list/appphoto.component';
import { AppphotoDetailComponent } from './detail/appphoto-detail.component';
import { AppphotoUpdateComponent } from './update/appphoto-update.component';
import { AppphotoDeleteDialogComponent } from './delete/appphoto-delete-dialog.component';
import { AppphotoRoutingModule } from './route/appphoto-routing.module';

@NgModule({
  imports: [SharedModule, AppphotoRoutingModule],
  declarations: [AppphotoComponent, AppphotoDetailComponent, AppphotoUpdateComponent, AppphotoDeleteDialogComponent],
  entryComponents: [AppphotoDeleteDialogComponent],
})
export class AppphotoModule {}
