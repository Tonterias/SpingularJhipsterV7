import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICceleb, Cceleb } from '../cceleb.model';
import { CcelebService } from '../service/cceleb.service';

@Injectable({ providedIn: 'root' })
export class CcelebRoutingResolveService implements Resolve<ICceleb> {
  constructor(protected service: CcelebService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICceleb> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((cceleb: HttpResponse<Cceleb>) => {
          if (cceleb.body) {
            return of(cceleb.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Cceleb());
  }
}
