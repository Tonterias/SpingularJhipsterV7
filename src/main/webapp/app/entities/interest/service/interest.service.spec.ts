import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IInterest, Interest } from '../interest.model';

import { InterestService } from './interest.service';

describe('Service Tests', () => {
  describe('Interest Service', () => {
    let service: InterestService;
    let httpMock: HttpTestingController;
    let elemDefault: IInterest;
    let expectedResult: IInterest | IInterest[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(InterestService);
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

      it('should create a Interest', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Interest()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Interest', () => {
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

      it('should partial update a Interest', () => {
        const patchObject = Object.assign({}, new Interest());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Interest', () => {
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

      it('should delete a Interest', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addInterestToCollectionIfMissing', () => {
        it('should add a Interest to an empty array', () => {
          const interest: IInterest = { id: 123 };
          expectedResult = service.addInterestToCollectionIfMissing([], interest);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(interest);
        });

        it('should not add a Interest to an array that contains it', () => {
          const interest: IInterest = { id: 123 };
          const interestCollection: IInterest[] = [
            {
              ...interest,
            },
            { id: 456 },
          ];
          expectedResult = service.addInterestToCollectionIfMissing(interestCollection, interest);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Interest to an array that doesn't contain it", () => {
          const interest: IInterest = { id: 123 };
          const interestCollection: IInterest[] = [{ id: 456 }];
          expectedResult = service.addInterestToCollectionIfMissing(interestCollection, interest);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(interest);
        });

        it('should add only unique Interest to an array', () => {
          const interestArray: IInterest[] = [{ id: 123 }, { id: 456 }, { id: 60684 }];
          const interestCollection: IInterest[] = [{ id: 123 }];
          expectedResult = service.addInterestToCollectionIfMissing(interestCollection, ...interestArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const interest: IInterest = { id: 123 };
          const interest2: IInterest = { id: 456 };
          expectedResult = service.addInterestToCollectionIfMissing([], interest, interest2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(interest);
          expect(expectedResult).toContain(interest2);
        });

        it('should accept null and undefined values', () => {
          const interest: IInterest = { id: 123 };
          expectedResult = service.addInterestToCollectionIfMissing([], null, interest, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(interest);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
