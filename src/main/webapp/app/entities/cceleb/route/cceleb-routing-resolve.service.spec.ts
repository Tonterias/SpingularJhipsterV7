jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICceleb, Cceleb } from '../cceleb.model';
import { CcelebService } from '../service/cceleb.service';

import { CcelebRoutingResolveService } from './cceleb-routing-resolve.service';

describe('Service Tests', () => {
  describe('Cceleb routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: CcelebRoutingResolveService;
    let service: CcelebService;
    let resultCceleb: ICceleb | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(CcelebRoutingResolveService);
      service = TestBed.inject(CcelebService);
      resultCceleb = undefined;
    });

    describe('resolve', () => {
      it('should return ICceleb returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCceleb = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCceleb).toEqual({ id: 123 });
      });

      it('should return new ICceleb if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCceleb = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultCceleb).toEqual(new Cceleb());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCceleb = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCceleb).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
