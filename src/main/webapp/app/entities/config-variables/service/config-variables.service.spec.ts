import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IConfigVariables, ConfigVariables } from '../config-variables.model';

import { ConfigVariablesService } from './config-variables.service';

describe('Service Tests', () => {
  describe('ConfigVariables Service', () => {
    let service: ConfigVariablesService;
    let httpMock: HttpTestingController;
    let elemDefault: IConfigVariables;
    let expectedResult: IConfigVariables | IConfigVariables[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ConfigVariablesService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        configVarLong1: 0,
        configVarLong2: 0,
        configVarLong3: 0,
        configVarLong4: 0,
        configVarLong5: 0,
        configVarLong6: 0,
        configVarLong7: 0,
        configVarLong8: 0,
        configVarLong9: 0,
        configVarLong10: 0,
        configVarLong11: 0,
        configVarLong12: 0,
        configVarLong13: 0,
        configVarLong14: 0,
        configVarLong15: 0,
        configVarBoolean16: false,
        configVarBoolean17: false,
        configVarBoolean18: false,
        configVarString19: 'AAAAAAA',
        configVarString20: 'AAAAAAA',
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

      it('should create a ConfigVariables', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new ConfigVariables()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ConfigVariables', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            configVarLong1: 1,
            configVarLong2: 1,
            configVarLong3: 1,
            configVarLong4: 1,
            configVarLong5: 1,
            configVarLong6: 1,
            configVarLong7: 1,
            configVarLong8: 1,
            configVarLong9: 1,
            configVarLong10: 1,
            configVarLong11: 1,
            configVarLong12: 1,
            configVarLong13: 1,
            configVarLong14: 1,
            configVarLong15: 1,
            configVarBoolean16: true,
            configVarBoolean17: true,
            configVarBoolean18: true,
            configVarString19: 'BBBBBB',
            configVarString20: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a ConfigVariables', () => {
        const patchObject = Object.assign(
          {
            configVarLong1: 1,
            configVarLong3: 1,
            configVarLong4: 1,
            configVarLong5: 1,
            configVarLong6: 1,
            configVarLong7: 1,
            configVarLong8: 1,
            configVarLong10: 1,
            configVarLong12: 1,
            configVarBoolean16: true,
            configVarBoolean17: true,
            configVarBoolean18: true,
            configVarString19: 'BBBBBB',
            configVarString20: 'BBBBBB',
          },
          new ConfigVariables()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ConfigVariables', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            configVarLong1: 1,
            configVarLong2: 1,
            configVarLong3: 1,
            configVarLong4: 1,
            configVarLong5: 1,
            configVarLong6: 1,
            configVarLong7: 1,
            configVarLong8: 1,
            configVarLong9: 1,
            configVarLong10: 1,
            configVarLong11: 1,
            configVarLong12: 1,
            configVarLong13: 1,
            configVarLong14: 1,
            configVarLong15: 1,
            configVarBoolean16: true,
            configVarBoolean17: true,
            configVarBoolean18: true,
            configVarString19: 'BBBBBB',
            configVarString20: 'BBBBBB',
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

      it('should delete a ConfigVariables', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addConfigVariablesToCollectionIfMissing', () => {
        it('should add a ConfigVariables to an empty array', () => {
          const configVariables: IConfigVariables = { id: 123 };
          expectedResult = service.addConfigVariablesToCollectionIfMissing([], configVariables);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(configVariables);
        });

        it('should not add a ConfigVariables to an array that contains it', () => {
          const configVariables: IConfigVariables = { id: 123 };
          const configVariablesCollection: IConfigVariables[] = [
            {
              ...configVariables,
            },
            { id: 456 },
          ];
          expectedResult = service.addConfigVariablesToCollectionIfMissing(configVariablesCollection, configVariables);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ConfigVariables to an array that doesn't contain it", () => {
          const configVariables: IConfigVariables = { id: 123 };
          const configVariablesCollection: IConfigVariables[] = [{ id: 456 }];
          expectedResult = service.addConfigVariablesToCollectionIfMissing(configVariablesCollection, configVariables);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(configVariables);
        });

        it('should add only unique ConfigVariables to an array', () => {
          const configVariablesArray: IConfigVariables[] = [{ id: 123 }, { id: 456 }, { id: 12505 }];
          const configVariablesCollection: IConfigVariables[] = [{ id: 123 }];
          expectedResult = service.addConfigVariablesToCollectionIfMissing(configVariablesCollection, ...configVariablesArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const configVariables: IConfigVariables = { id: 123 };
          const configVariables2: IConfigVariables = { id: 456 };
          expectedResult = service.addConfigVariablesToCollectionIfMissing([], configVariables, configVariables2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(configVariables);
          expect(expectedResult).toContain(configVariables2);
        });

        it('should accept null and undefined values', () => {
          const configVariables: IConfigVariables = { id: 123 };
          expectedResult = service.addConfigVariablesToCollectionIfMissing([], null, configVariables, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(configVariables);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
