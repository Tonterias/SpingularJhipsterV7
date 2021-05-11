import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IInterest, Interest } from '../interest.model';
import { InterestService } from '../service/interest.service';

@Injectable({ providedIn: 'root' })
export class InterestRoutingResolveService implements Resolve<IInterest> {
  constructor(protected service: InterestService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IInterest> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((interest: HttpResponse<Interest>) => {
          if (interest.body) {
            return of(interest.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Interest());
  }
}
