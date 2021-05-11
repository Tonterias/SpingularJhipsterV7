jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICeleb, Celeb } from '../celeb.model';
import { CelebService } from '../service/celeb.service';

import { CelebRoutingResolveService } from './celeb-routing-resolve.service';

describe('Service Tests', () => {
  describe('Celeb routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: CelebRoutingResolveService;
    let service: CelebService;
    let resultCeleb: ICeleb | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(CelebRoutingResolveService);
      service = TestBed.inject(CelebService);
      resultCeleb = undefined;
    });

    describe('resolve', () => {
      it('should return ICeleb returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCeleb = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCeleb).toEqual({ id: 123 });
      });

      it('should return new ICeleb if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCeleb = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultCeleb).toEqual(new Celeb());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCeleb = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCeleb).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
