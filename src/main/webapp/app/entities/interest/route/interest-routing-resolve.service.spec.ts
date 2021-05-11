jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IInterest, Interest } from '../interest.model';
import { InterestService } from '../service/interest.service';

import { InterestRoutingResolveService } from './interest-routing-resolve.service';

describe('Service Tests', () => {
  describe('Interest routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: InterestRoutingResolveService;
    let service: InterestService;
    let resultInterest: IInterest | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(InterestRoutingResolveService);
      service = TestBed.inject(InterestService);
      resultInterest = undefined;
    });

    describe('resolve', () => {
      it('should return IInterest returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultInterest = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultInterest).toEqual({ id: 123 });
      });

      it('should return new IInterest if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultInterest = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultInterest).toEqual(new Interest());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultInterest = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultInterest).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
