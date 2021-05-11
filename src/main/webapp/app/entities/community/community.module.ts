import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { CommunityComponent } from './list/community.component';
import { CommunityDetailComponent } from './detail/community-detail.component';
import { CommunityUpdateComponent } from './update/community-update.component';
import { CommunityDeleteDialogComponent } from './delete/community-delete-dialog.component';
import { CommunityRoutingModule } from './route/community-routing.module';

@NgModule({
  imports: [SharedModule, CommunityRoutingModule],
  declarations: [CommunityComponent, CommunityDetailComponent, CommunityUpdateComponent, CommunityDeleteDialogComponent],
  entryComponents: [CommunityDeleteDialogComponent],
})
export class CommunityModule {}
