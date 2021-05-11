jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICinterest, Cinterest } from '../cinterest.model';
import { CinterestService } from '../service/cinterest.service';

import { CinterestRoutingResolveService } from './cinterest-routing-resolve.service';

describe('Service Tests', () => {
  describe('Cinterest routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: CinterestRoutingResolveService;
    let service: CinterestService;
    let resultCinterest: ICinterest | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(CinterestRoutingResolveService);
      service = TestBed.inject(CinterestService);
      resultCinterest = undefined;
    });

    describe('resolve', () => {
      it('should return ICinterest returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCinterest = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCinterest).toEqual({ id: 123 });
      });

      it('should return new ICinterest if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCinterest = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultCinterest).toEqual(new Cinterest());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCinterest = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCinterest).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
