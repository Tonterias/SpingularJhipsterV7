import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAppphoto, Appphoto } from '../appphoto.model';

import { AppphotoService } from './appphoto.service';

describe('Service Tests', () => {
  describe('Appphoto Service', () => {
    let service: AppphotoService;
    let httpMock: HttpTestingController;
    let elemDefault: IAppphoto;
    let expectedResult: IAppphoto | IAppphoto[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(AppphotoService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        creationDate: currentDate,
        imageContentType: 'image/png',
        image: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            creationDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Appphoto', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            creationDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            creationDate: currentDate,
          },
          returnedFromService
        );

        service.create(new Appphoto()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Appphoto', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            creationDate: currentDate.format(DATE_TIME_FORMAT),
            image: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            creationDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Appphoto', () => {
        const patchObject = Object.assign(
          {
            creationDate: currentDate.format(DATE_TIME_FORMAT),
          },
          new Appphoto()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            creationDate: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Appphoto', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            creationDate: currentDate.format(DATE_TIME_FORMAT),
            image: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            creationDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Appphoto', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addAppphotoToCollectionIfMissing', () => {
        it('should add a Appphoto to an empty array', () => {
          const appphoto: IAppphoto = { id: 123 };
          expectedResult = service.addAppphotoToCollectionIfMissing([], appphoto);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(appphoto);
        });

        it('should not add a Appphoto to an array that contains it', () => {
          const appphoto: IAppphoto = { id: 123 };
          const appphotoCollection: IAppphoto[] = [
            {
              ...appphoto,
            },
            { id: 456 },
          ];
          expectedResult = service.addAppphotoToCollectionIfMissing(appphotoCollection, appphoto);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Appphoto to an array that doesn't contain it", () => {
          const appphoto: IAppphoto = { id: 123 };
          const appphotoCollection: IAppphoto[] = [{ id: 456 }];
          expectedResult = service.addAppphotoToCollectionIfMissing(appphotoCollection, appphoto);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(appphoto);
        });

        it('should add only unique Appphoto to an array', () => {
          const appphotoArray: IAppphoto[] = [{ id: 123 }, { id: 456 }, { id: 31536 }];
          const appphotoCollection: IAppphoto[] = [{ id: 123 }];
          expectedResult = service.addAppphotoToCollectionIfMissing(appphotoCollection, ...appphotoArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const appphoto: IAppphoto = { id: 123 };
          const appphoto2: IAppphoto = { id: 456 };
          expectedResult = service.addAppphotoToCollectionIfMissing([], appphoto, appphoto2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(appphoto);
          expect(expectedResult).toContain(appphoto2);
        });

        it('should accept null and undefined values', () => {
          const appphoto: IAppphoto = { id: 123 };
          expectedResult = service.addAppphotoToCollectionIfMissing([], null, appphoto, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(appphoto);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
