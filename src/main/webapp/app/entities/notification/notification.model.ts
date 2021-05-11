import * as dayjs from 'dayjs';
import { IAppuser } from 'app/entities/appuser/appuser.model';
import { NotificationReason } from 'app/entities/enumerations/notification-reason.model';

export interface INotification {
  id?: number;
  creationDate?: dayjs.Dayjs;
  notificationDate?: dayjs.Dayjs | null;
  notificationReason?: NotificationReason;
  notificationText?: string | null;
  isDelivered?: boolean | null;
  appuser?: IAppuser;
}

export class Notification implements INotification {
  constructor(
    public id?: number,
    public creationDate?: dayjs.Dayjs,
    public notificationDate?: dayjs.Dayjs | null,
    public notificationReason?: NotificationReason,
    public notificationText?: string | null,
    public isDelivered?: boolean | null,
    public appuser?: IAppuser
  ) {
    this.isDelivered = this.isDelivered ?? false;
  }
}

export function getNotificationIdentifier(notification: INotification): number | undefined {
  return notification.id;
}
