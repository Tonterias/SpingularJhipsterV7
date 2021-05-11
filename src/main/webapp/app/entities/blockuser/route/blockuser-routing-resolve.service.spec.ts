jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IBlockuser, Blockuser } from '../blockuser.model';
import { BlockuserService } from '../service/blockuser.service';

import { BlockuserRoutingResolveService } from './blockuser-routing-resolve.service';

describe('Service Tests', () => {
  describe('Blockuser routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: BlockuserRoutingResolveService;
    let service: BlockuserService;
    let resultBlockuser: IBlockuser | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(BlockuserRoutingResolveService);
      service = TestBed.inject(BlockuserService);
      resultBlockuser = undefined;
    });

    describe('resolve', () => {
      it('should return IBlockuser returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultBlockuser = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultBlockuser).toEqual({ id: 123 });
      });

      it('should return new IBlockuser if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultBlockuser = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultBlockuser).toEqual(new Blockuser());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultBlockuser = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultBlockuser).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
