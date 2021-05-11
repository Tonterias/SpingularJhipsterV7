import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IConfigVariables, ConfigVariables } from '../config-variables.model';
import { ConfigVariablesService } from '../service/config-variables.service';

@Injectable({ providedIn: 'root' })
export class ConfigVariablesRoutingResolveService implements Resolve<IConfigVariables> {
  constructor(protected service: ConfigVariablesService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IConfigVariables> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((configVariables: HttpResponse<ConfigVariables>) => {
          if (configVariables.body) {
            return of(configVariables.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ConfigVariables());
  }
}
