import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICactivity, Cactivity } from '../cactivity.model';
import { CactivityService } from '../service/cactivity.service';

@Injectable({ providedIn: 'root' })
export class CactivityRoutingResolveService implements Resolve<ICactivity> {
  constructor(protected service: CactivityService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICactivity> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((cactivity: HttpResponse<Cactivity>) => {
          if (cactivity.body) {
            return of(cactivity.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Cactivity());
  }
}
