import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICeleb, Celeb } from '../celeb.model';
import { CelebService } from '../service/celeb.service';

@Injectable({ providedIn: 'root' })
export class CelebRoutingResolveService implements Resolve<ICeleb> {
  constructor(protected service: CelebService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICeleb> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((celeb: HttpResponse<Celeb>) => {
          if (celeb.body) {
            return of(celeb.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Celeb());
  }
}
