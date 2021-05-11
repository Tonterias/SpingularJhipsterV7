import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAppphoto, Appphoto } from '../appphoto.model';
import { AppphotoService } from '../service/appphoto.service';

@Injectable({ providedIn: 'root' })
export class AppphotoRoutingResolveService implements Resolve<IAppphoto> {
  constructor(protected service: AppphotoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAppphoto> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((appphoto: HttpResponse<Appphoto>) => {
          if (appphoto.body) {
            return of(appphoto.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Appphoto());
  }
}
