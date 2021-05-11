import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAppuser, Appuser } from '../appuser.model';

import { AppuserService } from './appuser.service';

describe('Service Tests', () => {
  describe('Appuser Service', () => {
    let service: AppuserService;
    let httpMock: HttpTestingController;
    let elemDefault: IAppuser;
    let expectedResult: IAppuser | IAppuser[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(AppuserService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        creationDate: currentDate,
        bio: 'AAAAAAA',
        facebook: 'AAAAAAA',
        twitter: 'AAAAAAA',
        linkedin: 'AAAAAAA',
        instagram: 'AAAAAAA',
        birthdate: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            creationDate: currentDate.format(DATE_TIME_FORMAT),
            birthdate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Appuser', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            creationDate: currentDate.format(DATE_TIME_FORMAT),
            birthdate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            creationDate: currentDate,
            birthdate: currentDate,
          },
          returnedFromService
        );

        service.create(new Appuser()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Appuser', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            creationDate: currentDate.format(DATE_TIME_FORMAT),
            bio: 'BBBBBB',
            facebook: 'BBBBBB',
            twitter: 'BBBBBB',
            linkedin: 'BBBBBB',
            instagram: 'BBBBBB',
            birthdate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            creationDate: currentDate,
            birthdate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Appuser', () => {
        const patchObject = Object.assign(
          {
            bio: 'BBBBBB',
            twitter: 'BBBBBB',
            instagram: 'BBBBBB',
            birthdate: currentDate.format(DATE_TIME_FORMAT),
          },
          new Appuser()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            creationDate: currentDate,
            birthdate: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Appuser', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            creationDate: currentDate.format(DATE_TIME_FORMAT),
            bio: 'BBBBBB',
            facebook: 'BBBBBB',
            twitter: 'BBBBBB',
            linkedin: 'BBBBBB',
            instagram: 'BBBBBB',
            birthdate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            creationDate: currentDate,
            birthdate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Appuser', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addAppuserToCollectionIfMissing', () => {
        it('should add a Appuser to an empty array', () => {
          const appuser: IAppuser = { id: 123 };
          expectedResult = service.addAppuserToCollectionIfMissing([], appuser);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(appuser);
        });

        it('should not add a Appuser to an array that contains it', () => {
          const appuser: IAppuser = { id: 123 };
          const appuserCollection: IAppuser[] = [
            {
              ...appuser,
            },
            { id: 456 },
          ];
          expectedResult = service.addAppuserToCollectionIfMissing(appuserCollection, appuser);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Appuser to an array that doesn't contain it", () => {
          const appuser: IAppuser = { id: 123 };
          const appuserCollection: IAppuser[] = [{ id: 456 }];
          expectedResult = service.addAppuserToCollectionIfMissing(appuserCollection, appuser);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(appuser);
        });

        it('should add only unique Appuser to an array', () => {
          const appuserArray: IAppuser[] = [{ id: 123 }, { id: 456 }, { id: 98651 }];
          const appuserCollection: IAppuser[] = [{ id: 123 }];
          expectedResult = service.addAppuserToCollectionIfMissing(appuserCollection, ...appuserArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const appuser: IAppuser = { id: 123 };
          const appuser2: IAppuser = { id: 456 };
          expectedResult = service.addAppuserToCollectionIfMissing([], appuser, appuser2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(appuser);
          expect(expectedResult).toContain(appuser2);
        });

        it('should accept null and undefined values', () => {
          const appuser: IAppuser = { id: 123 };
          expectedResult = service.addAppuserToCollectionIfMissing([], null, appuser, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(appuser);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
