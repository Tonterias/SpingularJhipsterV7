import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { NotificationReason } from 'app/entities/enumerations/notification-reason.model';
import { INotification, Notification } from '../notification.model';

import { NotificationService } from './notification.service';

describe('Service Tests', () => {
  describe('Notification Service', () => {
    let service: NotificationService;
    let httpMock: HttpTestingController;
    let elemDefault: INotification;
    let expectedResult: INotification | INotification[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(NotificationService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        creationDate: currentDate,
        notificationDate: currentDate,
        notificationReason: NotificationReason.FOLLOWING,
        notificationText: 'AAAAAAA',
        isDelivered: false,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            creationDate: currentDate.format(DATE_TIME_FORMAT),
            notificationDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Notification', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            creationDate: currentDate.format(DATE_TIME_FORMAT),
            notificationDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            creationDate: currentDate,
            notificationDate: currentDate,
          },
          returnedFromService
        );

        service.create(new Notification()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Notification', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            creationDate: currentDate.format(DATE_TIME_FORMAT),
            notificationDate: currentDate.format(DATE_TIME_FORMAT),
            notificationReason: 'BBBBBB',
            notificationText: 'BBBBBB',
            isDelivered: true,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            creationDate: currentDate,
            notificationDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Notification', () => {
        const patchObject = Object.assign({}, new Notification());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            creationDate: currentDate,
            notificationDate: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Notification', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            creationDate: currentDate.format(DATE_TIME_FORMAT),
            notificationDate: currentDate.format(DATE_TIME_FORMAT),
            notificationReason: 'BBBBBB',
            notificationText: 'BBBBBB',
            isDelivered: true,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            creationDate: currentDate,
            notificationDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Notification', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addNotificationToCollectionIfMissing', () => {
        it('should add a Notification to an empty array', () => {
          const notification: INotification = { id: 123 };
          expectedResult = service.addNotificationToCollectionIfMissing([], notification);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(notification);
        });

        it('should not add a Notification to an array that contains it', () => {
          const notification: INotification = { id: 123 };
          const notificationCollection: INotification[] = [
            {
              ...notification,
            },
            { id: 456 },
          ];
          expectedResult = service.addNotificationToCollectionIfMissing(notificationCollection, notification);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Notification to an array that doesn't contain it", () => {
          const notification: INotification = { id: 123 };
          const notificationCollection: INotification[] = [{ id: 456 }];
          expectedResult = service.addNotificationToCollectionIfMissing(notificationCollection, notification);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(notification);
        });

        it('should add only unique Notification to an array', () => {
          const notificationArray: INotification[] = [{ id: 123 }, { id: 456 }, { id: 85985 }];
          const notificationCollection: INotification[] = [{ id: 123 }];
          expectedResult = service.addNotificationToCollectionIfMissing(notificationCollection, ...notificationArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const notification: INotification = { id: 123 };
          const notification2: INotification = { id: 456 };
          expectedResult = service.addNotificationToCollectionIfMissing([], notification, notification2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(notification);
          expect(expectedResult).toContain(notification2);
        });

        it('should accept null and undefined values', () => {
          const notification: INotification = { id: 123 };
          expectedResult = service.addNotificationToCollectionIfMissing([], null, notification, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(notification);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
