import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUrllink, Urllink } from '../urllink.model';
import { UrllinkService } from '../service/urllink.service';

@Injectable({ providedIn: 'root' })
export class UrllinkRoutingResolveService implements Resolve<IUrllink> {
  constructor(protected service: UrllinkService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IUrllink> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((urllink: HttpResponse<Urllink>) => {
          if (urllink.body) {
            return of(urllink.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Urllink());
  }
}
