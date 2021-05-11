jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IAppuser, Appuser } from '../appuser.model';
import { AppuserService } from '../service/appuser.service';

import { AppuserRoutingResolveService } from './appuser-routing-resolve.service';

describe('Service Tests', () => {
  describe('Appuser routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: AppuserRoutingResolveService;
    let service: AppuserService;
    let resultAppuser: IAppuser | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(AppuserRoutingResolveService);
      service = TestBed.inject(AppuserService);
      resultAppuser = undefined;
    });

    describe('resolve', () => {
      it('should return IAppuser returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAppuser = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultAppuser).toEqual({ id: 123 });
      });

      it('should return new IAppuser if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAppuser = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultAppuser).toEqual(new Appuser());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAppuser = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultAppuser).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
