import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICceleb, Cceleb } from '../cceleb.model';

import { CcelebService } from './cceleb.service';

describe('Service Tests', () => {
  describe('Cceleb Service', () => {
    let service: CcelebService;
    let httpMock: HttpTestingController;
    let elemDefault: ICceleb;
    let expectedResult: ICceleb | ICceleb[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CcelebService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        celebName: 'AAAAAAA',
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

      it('should create a Cceleb', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Cceleb()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Cceleb', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            celebName: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Cceleb', () => {
        const patchObject = Object.assign(
          {
            celebName: 'BBBBBB',
          },
          new Cceleb()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Cceleb', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            celebName: 'BBBBBB',
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

      it('should delete a Cceleb', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCcelebToCollectionIfMissing', () => {
        it('should add a Cceleb to an empty array', () => {
          const cceleb: ICceleb = { id: 123 };
          expectedResult = service.addCcelebToCollectionIfMissing([], cceleb);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(cceleb);
        });

        it('should not add a Cceleb to an array that contains it', () => {
          const cceleb: ICceleb = { id: 123 };
          const ccelebCollection: ICceleb[] = [
            {
              ...cceleb,
            },
            { id: 456 },
          ];
          expectedResult = service.addCcelebToCollectionIfMissing(ccelebCollection, cceleb);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Cceleb to an array that doesn't contain it", () => {
          const cceleb: ICceleb = { id: 123 };
          const ccelebCollection: ICceleb[] = [{ id: 456 }];
          expectedResult = service.addCcelebToCollectionIfMissing(ccelebCollection, cceleb);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(cceleb);
        });

        it('should add only unique Cceleb to an array', () => {
          const ccelebArray: ICceleb[] = [{ id: 123 }, { id: 456 }, { id: 5635 }];
          const ccelebCollection: ICceleb[] = [{ id: 123 }];
          expectedResult = service.addCcelebToCollectionIfMissing(ccelebCollection, ...ccelebArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const cceleb: ICceleb = { id: 123 };
          const cceleb2: ICceleb = { id: 456 };
          expectedResult = service.addCcelebToCollectionIfMissing([], cceleb, cceleb2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(cceleb);
          expect(expectedResult).toContain(cceleb2);
        });

        it('should accept null and undefined values', () => {
          const cceleb: ICceleb = { id: 123 };
          expectedResult = service.addCcelebToCollectionIfMissing([], null, cceleb, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(cceleb);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
