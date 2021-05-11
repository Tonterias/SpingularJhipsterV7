import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IFrontpageconfig, Frontpageconfig } from '../frontpageconfig.model';

import { FrontpageconfigService } from './frontpageconfig.service';

describe('Service Tests', () => {
  describe('Frontpageconfig Service', () => {
    let service: FrontpageconfigService;
    let httpMock: HttpTestingController;
    let elemDefault: IFrontpageconfig;
    let expectedResult: IFrontpageconfig | IFrontpageconfig[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(FrontpageconfigService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        creationDate: currentDate,
        topNews1: 0,
        topNews2: 0,
        topNews3: 0,
        topNews4: 0,
        topNews5: 0,
        latestNews1: 0,
        latestNews2: 0,
        latestNews3: 0,
        latestNews4: 0,
        latestNews5: 0,
        breakingNews1: 0,
        recentPosts1: 0,
        recentPosts2: 0,
        recentPosts3: 0,
        recentPosts4: 0,
        featuredArticles1: 0,
        featuredArticles2: 0,
        featuredArticles3: 0,
        featuredArticles4: 0,
        featuredArticles5: 0,
        featuredArticles6: 0,
        featuredArticles7: 0,
        featuredArticles8: 0,
        featuredArticles9: 0,
        featuredArticles10: 0,
        popularNews1: 0,
        popularNews2: 0,
        popularNews3: 0,
        popularNews4: 0,
        popularNews5: 0,
        popularNews6: 0,
        popularNews7: 0,
        popularNews8: 0,
        weeklyNews1: 0,
        weeklyNews2: 0,
        weeklyNews3: 0,
        weeklyNews4: 0,
        newsFeeds1: 0,
        newsFeeds2: 0,
        newsFeeds3: 0,
        newsFeeds4: 0,
        newsFeeds5: 0,
        newsFeeds6: 0,
        usefulLinks1: 0,
        usefulLinks2: 0,
        usefulLinks3: 0,
        usefulLinks4: 0,
        usefulLinks5: 0,
        usefulLinks6: 0,
        recentVideos1: 0,
        recentVideos2: 0,
        recentVideos3: 0,
        recentVideos4: 0,
        recentVideos5: 0,
        recentVideos6: 0,
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

      it('should create a Frontpageconfig', () => {
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

        service.create(new Frontpageconfig()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Frontpageconfig', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            creationDate: currentDate.format(DATE_TIME_FORMAT),
            topNews1: 1,
            topNews2: 1,
            topNews3: 1,
            topNews4: 1,
            topNews5: 1,
            latestNews1: 1,
            latestNews2: 1,
            latestNews3: 1,
            latestNews4: 1,
            latestNews5: 1,
            breakingNews1: 1,
            recentPosts1: 1,
            recentPosts2: 1,
            recentPosts3: 1,
            recentPosts4: 1,
            featuredArticles1: 1,
            featuredArticles2: 1,
            featuredArticles3: 1,
            featuredArticles4: 1,
            featuredArticles5: 1,
            featuredArticles6: 1,
            featuredArticles7: 1,
            featuredArticles8: 1,
            featuredArticles9: 1,
            featuredArticles10: 1,
            popularNews1: 1,
            popularNews2: 1,
            popularNews3: 1,
            popularNews4: 1,
            popularNews5: 1,
            popularNews6: 1,
            popularNews7: 1,
            popularNews8: 1,
            weeklyNews1: 1,
            weeklyNews2: 1,
            weeklyNews3: 1,
            weeklyNews4: 1,
            newsFeeds1: 1,
            newsFeeds2: 1,
            newsFeeds3: 1,
            newsFeeds4: 1,
            newsFeeds5: 1,
            newsFeeds6: 1,
            usefulLinks1: 1,
            usefulLinks2: 1,
            usefulLinks3: 1,
            usefulLinks4: 1,
            usefulLinks5: 1,
            usefulLinks6: 1,
            recentVideos1: 1,
            recentVideos2: 1,
            recentVideos3: 1,
            recentVideos4: 1,
            recentVideos5: 1,
            recentVideos6: 1,
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

      it('should partial update a Frontpageconfig', () => {
        const patchObject = Object.assign(
          {
            creationDate: currentDate.format(DATE_TIME_FORMAT),
            topNews1: 1,
            topNews3: 1,
            topNews5: 1,
            latestNews1: 1,
            latestNews3: 1,
            latestNews4: 1,
            latestNews5: 1,
            breakingNews1: 1,
            recentPosts2: 1,
            recentPosts3: 1,
            recentPosts4: 1,
            featuredArticles3: 1,
            popularNews6: 1,
            popularNews8: 1,
            weeklyNews2: 1,
            weeklyNews4: 1,
            newsFeeds2: 1,
            newsFeeds3: 1,
            newsFeeds4: 1,
            newsFeeds6: 1,
            recentVideos2: 1,
            recentVideos3: 1,
            recentVideos4: 1,
            recentVideos5: 1,
          },
          new Frontpageconfig()
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

      it('should return a list of Frontpageconfig', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            creationDate: currentDate.format(DATE_TIME_FORMAT),
            topNews1: 1,
            topNews2: 1,
            topNews3: 1,
            topNews4: 1,
            topNews5: 1,
            latestNews1: 1,
            latestNews2: 1,
            latestNews3: 1,
            latestNews4: 1,
            latestNews5: 1,
            breakingNews1: 1,
            recentPosts1: 1,
            recentPosts2: 1,
            recentPosts3: 1,
            recentPosts4: 1,
            featuredArticles1: 1,
            featuredArticles2: 1,
            featuredArticles3: 1,
            featuredArticles4: 1,
            featuredArticles5: 1,
            featuredArticles6: 1,
            featuredArticles7: 1,
            featuredArticles8: 1,
            featuredArticles9: 1,
            featuredArticles10: 1,
            popularNews1: 1,
            popularNews2: 1,
            popularNews3: 1,
            popularNews4: 1,
            popularNews5: 1,
            popularNews6: 1,
            popularNews7: 1,
            popularNews8: 1,
            weeklyNews1: 1,
            weeklyNews2: 1,
            weeklyNews3: 1,
            weeklyNews4: 1,
            newsFeeds1: 1,
            newsFeeds2: 1,
            newsFeeds3: 1,
            newsFeeds4: 1,
            newsFeeds5: 1,
            newsFeeds6: 1,
            usefulLinks1: 1,
            usefulLinks2: 1,
            usefulLinks3: 1,
            usefulLinks4: 1,
            usefulLinks5: 1,
            usefulLinks6: 1,
            recentVideos1: 1,
            recentVideos2: 1,
            recentVideos3: 1,
            recentVideos4: 1,
            recentVideos5: 1,
            recentVideos6: 1,
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

      it('should delete a Frontpageconfig', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addFrontpageconfigToCollectionIfMissing', () => {
        it('should add a Frontpageconfig to an empty array', () => {
          const frontpageconfig: IFrontpageconfig = { id: 123 };
          expectedResult = service.addFrontpageconfigToCollectionIfMissing([], frontpageconfig);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(frontpageconfig);
        });

        it('should not add a Frontpageconfig to an array that contains it', () => {
          const frontpageconfig: IFrontpageconfig = { id: 123 };
          const frontpageconfigCollection: IFrontpageconfig[] = [
            {
              ...frontpageconfig,
            },
            { id: 456 },
          ];
          expectedResult = service.addFrontpageconfigToCollectionIfMissing(frontpageconfigCollection, frontpageconfig);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Frontpageconfig to an array that doesn't contain it", () => {
          const frontpageconfig: IFrontpageconfig = { id: 123 };
          const frontpageconfigCollection: IFrontpageconfig[] = [{ id: 456 }];
          expectedResult = service.addFrontpageconfigToCollectionIfMissing(frontpageconfigCollection, frontpageconfig);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(frontpageconfig);
        });

        it('should add only unique Frontpageconfig to an array', () => {
          const frontpageconfigArray: IFrontpageconfig[] = [{ id: 123 }, { id: 456 }, { id: 61610 }];
          const frontpageconfigCollection: IFrontpageconfig[] = [{ id: 123 }];
          expectedResult = service.addFrontpageconfigToCollectionIfMissing(frontpageconfigCollection, ...frontpageconfigArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const frontpageconfig: IFrontpageconfig = { id: 123 };
          const frontpageconfig2: IFrontpageconfig = { id: 456 };
          expectedResult = service.addFrontpageconfigToCollectionIfMissing([], frontpageconfig, frontpageconfig2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(frontpageconfig);
          expect(expectedResult).toContain(frontpageconfig2);
        });

        it('should accept null and undefined values', () => {
          const frontpageconfig: IFrontpageconfig = { id: 123 };
          expectedResult = service.addFrontpageconfigToCollectionIfMissing([], null, frontpageconfig, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(frontpageconfig);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
