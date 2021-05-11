jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IComment, Comment } from '../comment.model';
import { CommentService } from '../service/comment.service';

import { CommentRoutingResolveService } from './comment-routing-resolve.service';

describe('Service Tests', () => {
  describe('Comment routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: CommentRoutingResolveService;
    let service: CommentService;
    let resultComment: IComment | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(CommentRoutingResolveService);
      service = TestBed.inject(CommentService);
      resultComment = undefined;
    });

    describe('resolve', () => {
      it('should return IComment returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultComment = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultComment).toEqual({ id: 123 });
      });

      it('should return new IComment if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultComment = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultComment).toEqual(new Comment());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultComment = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultComment).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
