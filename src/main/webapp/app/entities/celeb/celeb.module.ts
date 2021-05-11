import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { CelebComponent } from './list/celeb.component';
import { CelebDetailComponent } from './detail/celeb-detail.component';
import { CelebUpdateComponent } from './update/celeb-update.component';
import { CelebDeleteDialogComponent } from './delete/celeb-delete-dialog.component';
import { CelebRoutingModule } from './route/celeb-routing.module';

@NgModule({
  imports: [SharedModule, CelebRoutingModule],
  declarations: [CelebComponent, CelebDetailComponent, CelebUpdateComponent, CelebDeleteDialogComponent],
  entryComponents: [CelebDeleteDialogComponent],
})
export class CelebModule {}
