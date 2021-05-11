import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICactivity, Cactivity } from '../cactivity.model';

import { CactivityService } from './cactivity.service';

describe('Service Tests', () => {
  describe('Cactivity Service', () => {
    let service: CactivityService;
    let httpMock: HttpTestingController;
    let elemDefault: ICactivity;
    let expectedResult: ICactivity | ICactivity[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CactivityService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        activityName: 'AAAAAAA',
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

      it('should create a Cactivity', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Cactivity()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Cactivity', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            activityName: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Cactivity', () => {
        const patchObject = Object.assign({}, new Cactivity());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Cactivity', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            activityName: 'BBBBBB',
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

      it('should delete a Cactivity', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCactivityToCollectionIfMissing', () => {
        it('should add a Cactivity to an empty array', () => {
          const cactivity: ICactivity = { id: 123 };
          expectedResult = service.addCactivityToCollectionIfMissing([], cactivity);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(cactivity);
        });

        it('should not add a Cactivity to an array that contains it', () => {
          const cactivity: ICactivity = { id: 123 };
          const cactivityCollection: ICactivity[] = [
            {
              ...cactivity,
            },
            { id: 456 },
          ];
          expectedResult = service.addCactivityToCollectionIfMissing(cactivityCollection, cactivity);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Cactivity to an array that doesn't contain it", () => {
          const cactivity: ICactivity = { id: 123 };
          const cactivityCollection: ICactivity[] = [{ id: 456 }];
          expectedResult = service.addCactivityToCollectionIfMissing(cactivityCollection, cactivity);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(cactivity);
        });

        it('should add only unique Cactivity to an array', () => {
          const cactivityArray: ICactivity[] = [{ id: 123 }, { id: 456 }, { id: 21613 }];
          const cactivityCollection: ICactivity[] = [{ id: 123 }];
          expectedResult = service.addCactivityToCollectionIfMissing(cactivityCollection, ...cactivityArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const cactivity: ICactivity = { id: 123 };
          const cactivity2: ICactivity = { id: 456 };
          expectedResult = service.addCactivityToCollectionIfMissing([], cactivity, cactivity2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(cactivity);
          expect(expectedResult).toContain(cactivity2);
        });

        it('should accept null and undefined values', () => {
          const cactivity: ICactivity = { id: 123 };
          expectedResult = service.addCactivityToCollectionIfMissing([], null, cactivity, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(cactivity);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
