import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IComment, Comment } from '../comment.model';

import { CommentService } from './comment.service';

describe('Service Tests', () => {
  describe('Comment Service', () => {
    let service: CommentService;
    let httpMock: HttpTestingController;
    let elemDefault: IComment;
    let expectedResult: IComment | IComment[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CommentService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        creationDate: currentDate,
        commentText: 'AAAAAAA',
        isOffensive: false,
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

      it('should create a Comment', () => {
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

        service.create(new Comment()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Comment', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            creationDate: currentDate.format(DATE_TIME_FORMAT),
            commentText: 'BBBBBB',
            isOffensive: true,
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

      it('should partial update a Comment', () => {
        const patchObject = Object.assign(
          {
            creationDate: currentDate.format(DATE_TIME_FORMAT),
            commentText: 'BBBBBB',
            isOffensive: true,
          },
          new Comment()
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

      it('should return a list of Comment', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            creationDate: currentDate.format(DATE_TIME_FORMAT),
            commentText: 'BBBBBB',
            isOffensive: true,
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

      it('should delete a Comment', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCommentToCollectionIfMissing', () => {
        it('should add a Comment to an empty array', () => {
          const comment: IComment = { id: 123 };
          expectedResult = service.addCommentToCollectionIfMissing([], comment);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(comment);
        });

        it('should not add a Comment to an array that contains it', () => {
          const comment: IComment = { id: 123 };
          const commentCollection: IComment[] = [
            {
              ...comment,
            },
            { id: 456 },
          ];
          expectedResult = service.addCommentToCollectionIfMissing(commentCollection, comment);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Comment to an array that doesn't contain it", () => {
          const comment: IComment = { id: 123 };
          const commentCollection: IComment[] = [{ id: 456 }];
          expectedResult = service.addCommentToCollectionIfMissing(commentCollection, comment);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(comment);
        });

        it('should add only unique Comment to an array', () => {
          const commentArray: IComment[] = [{ id: 123 }, { id: 456 }, { id: 7255 }];
          const commentCollection: IComment[] = [{ id: 123 }];
          expectedResult = service.addCommentToCollectionIfMissing(commentCollection, ...commentArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const comment: IComment = { id: 123 };
          const comment2: IComment = { id: 456 };
          expectedResult = service.addCommentToCollectionIfMissing([], comment, comment2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(comment);
          expect(expectedResult).toContain(comment2);
        });

        it('should accept null and undefined values', () => {
          const comment: IComment = { id: 123 };
          expectedResult = service.addCommentToCollectionIfMissing([], null, comment, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(comment);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
