import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAppuser, Appuser } from '../appuser.model';
import { AppuserService } from '../service/appuser.service';

@Injectable({ providedIn: 'root' })
export class AppuserRoutingResolveService implements Resolve<IAppuser> {
  constructor(protected service: AppuserService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAppuser> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((appuser: HttpResponse<Appuser>) => {
          if (appuser.body) {
            return of(appuser.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Appuser());
  }
}
