import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICinterest, Cinterest } from '../cinterest.model';
import { CinterestService } from '../service/cinterest.service';

@Injectable({ providedIn: 'root' })
export class CinterestRoutingResolveService implements Resolve<ICinterest> {
  constructor(protected service: CinterestService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICinterest> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((cinterest: HttpResponse<Cinterest>) => {
          if (cinterest.body) {
            return of(cinterest.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Cinterest());
  }
}
