import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IUrllink, Urllink } from '../urllink.model';

import { UrllinkService } from './urllink.service';

describe('Service Tests', () => {
  describe('Urllink Service', () => {
    let service: UrllinkService;
    let httpMock: HttpTestingController;
    let elemDefault: IUrllink;
    let expectedResult: IUrllink | IUrllink[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(UrllinkService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        linkText: 'AAAAAAA',
        linkURL: 'AAAAAAA',
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

      it('should create a Urllink', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Urllink()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Urllink', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            linkText: 'BBBBBB',
            linkURL: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Urllink', () => {
        const patchObject = Object.assign(
          {
            linkText: 'BBBBBB',
            linkURL: 'BBBBBB',
          },
          new Urllink()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Urllink', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            linkText: 'BBBBBB',
            linkURL: 'BBBBBB',
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

      it('should delete a Urllink', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addUrllinkToCollectionIfMissing', () => {
        it('should add a Urllink to an empty array', () => {
          const urllink: IUrllink = { id: 123 };
          expectedResult = service.addUrllinkToCollectionIfMissing([], urllink);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(urllink);
        });

        it('should not add a Urllink to an array that contains it', () => {
          const urllink: IUrllink = { id: 123 };
          const urllinkCollection: IUrllink[] = [
            {
              ...urllink,
            },
            { id: 456 },
          ];
          expectedResult = service.addUrllinkToCollectionIfMissing(urllinkCollection, urllink);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Urllink to an array that doesn't contain it", () => {
          const urllink: IUrllink = { id: 123 };
          const urllinkCollection: IUrllink[] = [{ id: 456 }];
          expectedResult = service.addUrllinkToCollectionIfMissing(urllinkCollection, urllink);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(urllink);
        });

        it('should add only unique Urllink to an array', () => {
          const urllinkArray: IUrllink[] = [{ id: 123 }, { id: 456 }, { id: 96403 }];
          const urllinkCollection: IUrllink[] = [{ id: 123 }];
          expectedResult = service.addUrllinkToCollectionIfMissing(urllinkCollection, ...urllinkArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const urllink: IUrllink = { id: 123 };
          const urllink2: IUrllink = { id: 456 };
          expectedResult = service.addUrllinkToCollectionIfMissing([], urllink, urllink2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(urllink);
          expect(expectedResult).toContain(urllink2);
        });

        it('should accept null and undefined values', () => {
          const urllink: IUrllink = { id: 123 };
          expectedResult = service.addUrllinkToCollectionIfMissing([], null, urllink, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(urllink);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
