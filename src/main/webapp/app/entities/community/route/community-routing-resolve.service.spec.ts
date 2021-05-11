jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICommunity, Community } from '../community.model';
import { CommunityService } from '../service/community.service';

import { CommunityRoutingResolveService } from './community-routing-resolve.service';

describe('Service Tests', () => {
  describe('Community routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: CommunityRoutingResolveService;
    let service: CommunityService;
    let resultCommunity: ICommunity | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(CommunityRoutingResolveService);
      service = TestBed.inject(CommunityService);
      resultCommunity = undefined;
    });

    describe('resolve', () => {
      it('should return ICommunity returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCommunity = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCommunity).toEqual({ id: 123 });
      });

      it('should return new ICommunity if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCommunity = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultCommunity).toEqual(new Community());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCommunity = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCommunity).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
