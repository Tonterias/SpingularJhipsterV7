import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IFeedback, Feedback } from '../feedback.model';

import { FeedbackService } from './feedback.service';

describe('Service Tests', () => {
  describe('Feedback Service', () => {
    let service: FeedbackService;
    let httpMock: HttpTestingController;
    let elemDefault: IFeedback;
    let expectedResult: IFeedback | IFeedback[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(FeedbackService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        creationDate: currentDate,
        name: 'AAAAAAA',
        email: 'AAAAAAA',
        feedback: 'AAAAAAA',
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

      it('should create a Feedback', () => {
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

        service.create(new Feedback()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Feedback', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            creationDate: currentDate.format(DATE_TIME_FORMAT),
            name: 'BBBBBB',
            email: 'BBBBBB',
            feedback: 'BBBBBB',
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

      it('should partial update a Feedback', () => {
        const patchObject = Object.assign(
          {
            creationDate: currentDate.format(DATE_TIME_FORMAT),
            name: 'BBBBBB',
            email: 'BBBBBB',
          },
          new Feedback()
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

      it('should return a list of Feedback', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            creationDate: currentDate.format(DATE_TIME_FORMAT),
            name: 'BBBBBB',
            email: 'BBBBBB',
            feedback: 'BBBBBB',
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

      it('should delete a Feedback', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addFeedbackToCollectionIfMissing', () => {
        it('should add a Feedback to an empty array', () => {
          const feedback: IFeedback = { id: 123 };
          expectedResult = service.addFeedbackToCollectionIfMissing([], feedback);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(feedback);
        });

        it('should not add a Feedback to an array that contains it', () => {
          const feedback: IFeedback = { id: 123 };
          const feedbackCollection: IFeedback[] = [
            {
              ...feedback,
            },
            { id: 456 },
          ];
          expectedResult = service.addFeedbackToCollectionIfMissing(feedbackCollection, feedback);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Feedback to an array that doesn't contain it", () => {
          const feedback: IFeedback = { id: 123 };
          const feedbackCollection: IFeedback[] = [{ id: 456 }];
          expectedResult = service.addFeedbackToCollectionIfMissing(feedbackCollection, feedback);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(feedback);
        });

        it('should add only unique Feedback to an array', () => {
          const feedbackArray: IFeedback[] = [{ id: 123 }, { id: 456 }, { id: 80588 }];
          const feedbackCollection: IFeedback[] = [{ id: 123 }];
          expectedResult = service.addFeedbackToCollectionIfMissing(feedbackCollection, ...feedbackArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const feedback: IFeedback = { id: 123 };
          const feedback2: IFeedback = { id: 456 };
          expectedResult = service.addFeedbackToCollectionIfMissing([], feedback, feedback2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(feedback);
          expect(expectedResult).toContain(feedback2);
        });

        it('should accept null and undefined values', () => {
          const feedback: IFeedback = { id: 123 };
          expectedResult = service.addFeedbackToCollectionIfMissing([], null, feedback, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(feedback);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
