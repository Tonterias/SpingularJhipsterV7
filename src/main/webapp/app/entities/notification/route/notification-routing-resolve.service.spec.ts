jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { INotification, Notification } from '../notification.model';
import { NotificationService } from '../service/notification.service';

import { NotificationRoutingResolveService } from './notification-routing-resolve.service';

describe('Service Tests', () => {
  describe('Notification routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: NotificationRoutingResolveService;
    let service: NotificationService;
    let resultNotification: INotification | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(NotificationRoutingResolveService);
      service = TestBed.inject(NotificationService);
      resultNotification = undefined;
    });

    describe('resolve', () => {
      it('should return INotification returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultNotification = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultNotification).toEqual({ id: 123 });
      });

      it('should return new INotification if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultNotification = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultNotification).toEqual(new Notification());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultNotification = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultNotification).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
