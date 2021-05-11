jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IAppphoto, Appphoto } from '../appphoto.model';
import { AppphotoService } from '../service/appphoto.service';

import { AppphotoRoutingResolveService } from './appphoto-routing-resolve.service';

describe('Service Tests', () => {
  describe('Appphoto routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: AppphotoRoutingResolveService;
    let service: AppphotoService;
    let resultAppphoto: IAppphoto | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(AppphotoRoutingResolveService);
      service = TestBed.inject(AppphotoService);
      resultAppphoto = undefined;
    });

    describe('resolve', () => {
      it('should return IAppphoto returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAppphoto = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultAppphoto).toEqual({ id: 123 });
      });

      it('should return new IAppphoto if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAppphoto = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultAppphoto).toEqual(new Appphoto());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAppphoto = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultAppphoto).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
