import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IBlockuser, Blockuser } from '../blockuser.model';

import { BlockuserService } from './blockuser.service';

describe('Service Tests', () => {
  describe('Blockuser Service', () => {
    let service: BlockuserService;
    let httpMock: HttpTestingController;
    let elemDefault: IBlockuser;
    let expectedResult: IBlockuser | IBlockuser[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(BlockuserService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        creationDate: currentDate,
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

      it('should create a Blockuser', () => {
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

        service.create(new Blockuser()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Blockuser', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
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

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Blockuser', () => {
        const patchObject = Object.assign({}, new Blockuser());

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

      it('should return a list of Blockuser', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
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

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Blockuser', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addBlockuserToCollectionIfMissing', () => {
        it('should add a Blockuser to an empty array', () => {
          const blockuser: IBlockuser = { id: 123 };
          expectedResult = service.addBlockuserToCollectionIfMissing([], blockuser);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(blockuser);
        });

        it('should not add a Blockuser to an array that contains it', () => {
          const blockuser: IBlockuser = { id: 123 };
          const blockuserCollection: IBlockuser[] = [
            {
              ...blockuser,
            },
            { id: 456 },
          ];
          expectedResult = service.addBlockuserToCollectionIfMissing(blockuserCollection, blockuser);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Blockuser to an array that doesn't contain it", () => {
          const blockuser: IBlockuser = { id: 123 };
          const blockuserCollection: IBlockuser[] = [{ id: 456 }];
          expectedResult = service.addBlockuserToCollectionIfMissing(blockuserCollection, blockuser);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(blockuser);
        });

        it('should add only unique Blockuser to an array', () => {
          const blockuserArray: IBlockuser[] = [{ id: 123 }, { id: 456 }, { id: 18621 }];
          const blockuserCollection: IBlockuser[] = [{ id: 123 }];
          expectedResult = service.addBlockuserToCollectionIfMissing(blockuserCollection, ...blockuserArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const blockuser: IBlockuser = { id: 123 };
          const blockuser2: IBlockuser = { id: 456 };
          expectedResult = service.addBlockuserToCollectionIfMissing([], blockuser, blockuser2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(blockuser);
          expect(expectedResult).toContain(blockuser2);
        });

        it('should accept null and undefined values', () => {
          const blockuser: IBlockuser = { id: 123 };
          expectedResult = service.addBlockuserToCollectionIfMissing([], null, blockuser, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(blockuser);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
