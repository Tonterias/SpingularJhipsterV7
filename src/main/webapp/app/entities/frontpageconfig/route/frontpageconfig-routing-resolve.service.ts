import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFrontpageconfig, Frontpageconfig } from '../frontpageconfig.model';
import { FrontpageconfigService } from '../service/frontpageconfig.service';

@Injectable({ providedIn: 'root' })
export class FrontpageconfigRoutingResolveService implements Resolve<IFrontpageconfig> {
  constructor(protected service: FrontpageconfigService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFrontpageconfig> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((frontpageconfig: HttpResponse<Frontpageconfig>) => {
          if (frontpageconfig.body) {
            return of(frontpageconfig.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Frontpageconfig());
  }
}
