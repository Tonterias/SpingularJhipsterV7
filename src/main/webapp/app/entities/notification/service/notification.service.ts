import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { INotification, getNotificationIdentifier } from '../notification.model';

export type EntityResponseType = HttpResponse<INotification>;
export type EntityArrayResponseType = HttpResponse<INotification[]>;

@Injectable({ providedIn: 'root' })
export class NotificationService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/notifications');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(notification: INotification): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(notification);
    return this.http
      .post<INotification>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(notification: INotification): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(notification);
    return this.http
      .put<INotification>(`${this.resourceUrl}/${getNotificationIdentifier(notification) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(notification: INotification): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(notification);
    return this.http
      .patch<INotification>(`${this.resourceUrl}/${getNotificationIdentifier(notification) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<INotification>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<INotification[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addNotificationToCollectionIfMissing(
    notificationCollection: INotification[],
    ...notificationsToCheck: (INotification | null | undefined)[]
  ): INotification[] {
    const notifications: INotification[] = notificationsToCheck.filter(isPresent);
    if (notifications.length > 0) {
      const notificationCollectionIdentifiers = notificationCollection.map(
        notificationItem => getNotificationIdentifier(notificationItem)!
      );
      const notificationsToAdd = notifications.filter(notificationItem => {
        const notificationIdentifier = getNotificationIdentifier(notificationItem);
        if (notificationIdentifier == null || notificationCollectionIdentifiers.includes(notificationIdentifier)) {
          return false;
        }
        notificationCollectionIdentifiers.push(notificationIdentifier);
        return true;
      });
      return [...notificationsToAdd, ...notificationCollection];
    }
    return notificationCollection;
  }

  protected convertDateFromClient(notification: INotification): INotification {
    return Object.assign({}, notification, {
      creationDate: notification.creationDate?.isValid() ? notification.creationDate.toJSON() : undefined,
      notificationDate: notification.notificationDate?.isValid() ? notification.notificationDate.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.creationDate = res.body.creationDate ? dayjs(res.body.creationDate) : undefined;
      res.body.notificationDate = res.body.notificationDate ? dayjs(res.body.notificationDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((notification: INotification) => {
        notification.creationDate = notification.creationDate ? dayjs(notification.creationDate) : undefined;
        notification.notificationDate = notification.notificationDate ? dayjs(notification.notificationDate) : undefined;
      });
    }
    return res;
  }
}
