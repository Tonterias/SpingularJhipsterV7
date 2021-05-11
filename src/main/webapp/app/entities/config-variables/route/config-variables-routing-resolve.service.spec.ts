jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IConfigVariables, ConfigVariables } from '../config-variables.model';
import { ConfigVariablesService } from '../service/config-variables.service';

import { ConfigVariablesRoutingResolveService } from './config-variables-routing-resolve.service';

describe('Service Tests', () => {
  describe('ConfigVariables routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ConfigVariablesRoutingResolveService;
    let service: ConfigVariablesService;
    let resultConfigVariables: IConfigVariables | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ConfigVariablesRoutingResolveService);
      service = TestBed.inject(ConfigVariablesService);
      resultConfigVariables = undefined;
    });

    describe('resolve', () => {
      it('should return IConfigVariables returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultConfigVariables = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultConfigVariables).toEqual({ id: 123 });
      });

      it('should return new IConfigVariables if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultConfigVariables = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultConfigVariables).toEqual(new ConfigVariables());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultConfigVariables = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultConfigVariables).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
