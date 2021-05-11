import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICinterest, Cinterest } from '../cinterest.model';

import { CinterestService } from './cinterest.service';

describe('Service Tests', () => {
  describe('Cinterest Service', () => {
    let service: CinterestService;
    let httpMock: HttpTestingController;
    let elemDefault: ICinterest;
    let expectedResult: ICinterest | ICinterest[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CinterestService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        interestName: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Cinterest', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Cinterest()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Cinterest', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            interestName: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Cinterest', () => {
        const patchObject = Object.assign(
          {
            interestName: 'BBBBBB',
          },
          new Cinterest()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Cinterest', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            interestName: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Cinterest', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCinterestToCollectionIfMissing', () => {
        it('should add a Cinterest to an empty array', () => {
          const cinterest: ICinterest = { id: 123 };
          expectedResult = service.addCinterestToCollectionIfMissing([], cinterest);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(cinterest);
        });

        it('should not add a Cinterest to an array that contains it', () => {
          const cinterest: ICinterest = { id: 123 };
          const cinterestCollection: ICinterest[] = [
            {
              ...cinterest,
            },
            { id: 456 },
          ];
          expectedResult = service.addCinterestToCollectionIfMissing(cinterestCollection, cinterest);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Cinterest to an array that doesn't contain it", () => {
          const cinterest: ICinterest = { id: 123 };
          const cinterestCollection: ICinterest[] = [{ id: 456 }];
          expectedResult = service.addCinterestToCollectionIfMissing(cinterestCollection, cinterest);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(cinterest);
        });

        it('should add only unique Cinterest to an array', () => {
          const cinterestArray: ICinterest[] = [{ id: 123 }, { id: 456 }, { id: 20211 }];
          const cinterestCollection: ICinterest[] = [{ id: 123 }];
          expectedResult = service.addCinterestToCollectionIfMissing(cinterestCollection, ...cinterestArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const cinterest: ICinterest = { id: 123 };
          const cinterest2: ICinterest = { id: 456 };
          expectedResult = service.addCinterestToCollectionIfMissing([], cinterest, cinterest2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(cinterest);
          expect(expectedResult).toContain(cinterest2);
        });

        it('should accept null and undefined values', () => {
          const cinterest: ICinterest = { id: 123 };
          expectedResult = service.addCinterestToCollectionIfMissing([], null, cinterest, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(cinterest);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
