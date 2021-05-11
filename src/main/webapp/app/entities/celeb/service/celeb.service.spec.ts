import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICeleb, Celeb } from '../celeb.model';

import { CelebService } from './celeb.service';

describe('Service Tests', () => {
  describe('Celeb Service', () => {
    let service: CelebService;
    let httpMock: HttpTestingController;
    let elemDefault: ICeleb;
    let expectedResult: ICeleb | ICeleb[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CelebService);
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

      it('should create a Celeb', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Celeb()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Celeb', () => {
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

      it('should partial update a Celeb', () => {
        const patchObject = Object.assign(
          {
            celebName: 'BBBBBB',
          },
          new Celeb()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Celeb', () => {
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

      it('should delete a Celeb', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCelebToCollectionIfMissing', () => {
        it('should add a Celeb to an empty array', () => {
          const celeb: ICeleb = { id: 123 };
          expectedResult = service.addCelebToCollectionIfMissing([], celeb);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(celeb);
        });

        it('should not add a Celeb to an array that contains it', () => {
          const celeb: ICeleb = { id: 123 };
          const celebCollection: ICeleb[] = [
            {
              ...celeb,
            },
            { id: 456 },
          ];
          expectedResult = service.addCelebToCollectionIfMissing(celebCollection, celeb);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Celeb to an array that doesn't contain it", () => {
          const celeb: ICeleb = { id: 123 };
          const celebCollection: ICeleb[] = [{ id: 456 }];
          expectedResult = service.addCelebToCollectionIfMissing(celebCollection, celeb);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(celeb);
        });

        it('should add only unique Celeb to an array', () => {
          const celebArray: ICeleb[] = [{ id: 123 }, { id: 456 }, { id: 3161 }];
          const celebCollection: ICeleb[] = [{ id: 123 }];
          expectedResult = service.addCelebToCollectionIfMissing(celebCollection, ...celebArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const celeb: ICeleb = { id: 123 };
          const celeb2: ICeleb = { id: 456 };
          expectedResult = service.addCelebToCollectionIfMissing([], celeb, celeb2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(celeb);
          expect(expectedResult).toContain(celeb2);
        });

        it('should accept null and undefined values', () => {
          const celeb: ICeleb = { id: 123 };
          expectedResult = service.addCelebToCollectionIfMissing([], null, celeb, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(celeb);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
