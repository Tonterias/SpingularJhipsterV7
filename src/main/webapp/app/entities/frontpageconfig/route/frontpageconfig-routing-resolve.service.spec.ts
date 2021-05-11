jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IFrontpageconfig, Frontpageconfig } from '../frontpageconfig.model';
import { FrontpageconfigService } from '../service/frontpageconfig.service';

import { FrontpageconfigRoutingResolveService } from './frontpageconfig-routing-resolve.service';

describe('Service Tests', () => {
  describe('Frontpageconfig routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: FrontpageconfigRoutingResolveService;
    let service: FrontpageconfigService;
    let resultFrontpageconfig: IFrontpageconfig | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(FrontpageconfigRoutingResolveService);
      service = TestBed.inject(FrontpageconfigService);
      resultFrontpageconfig = undefined;
    });

    describe('resolve', () => {
      it('should return IFrontpageconfig returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFrontpageconfig = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultFrontpageconfig).toEqual({ id: 123 });
      });

      it('should return new IFrontpageconfig if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFrontpageconfig = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultFrontpageconfig).toEqual(new Frontpageconfig());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFrontpageconfig = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultFrontpageconfig).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
