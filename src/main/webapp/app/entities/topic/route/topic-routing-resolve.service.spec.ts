jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ITopic, Topic } from '../topic.model';
import { TopicService } from '../service/topic.service';

import { TopicRoutingResolveService } from './topic-routing-resolve.service';

describe('Service Tests', () => {
  describe('Topic routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: TopicRoutingResolveService;
    let service: TopicService;
    let resultTopic: ITopic | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(TopicRoutingResolveService);
      service = TestBed.inject(TopicService);
      resultTopic = undefined;
    });

    describe('resolve', () => {
      it('should return ITopic returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTopic = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTopic).toEqual({ id: 123 });
      });

      it('should return new ITopic if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTopic = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultTopic).toEqual(new Topic());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTopic = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTopic).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
