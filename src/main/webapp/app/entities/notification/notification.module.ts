import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { NotificationComponent } from './list/notification.component';
import { NotificationDetailComponent } from './detail/notification-detail.component';
import { NotificationUpdateComponent } from './update/notification-update.component';
import { NotificationDeleteDialogComponent } from './delete/notification-delete-dialog.component';
import { NotificationRoutingModule } from './route/notification-routing.module';

@NgModule({
  imports: [SharedModule, NotificationRoutingModule],
  declarations: [NotificationComponent, NotificationDetailComponent, NotificationUpdateComponent, NotificationDeleteDialogComponent],
  entryComponents: [NotificationDeleteDialogComponent],
})
export class NotificationModule {}
