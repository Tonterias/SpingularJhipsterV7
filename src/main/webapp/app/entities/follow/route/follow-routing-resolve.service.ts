import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFollow, Follow } from '../follow.model';
import { FollowService } from '../service/follow.service';

@Injectable({ providedIn: 'root' })
export class FollowRoutingResolveService implements Resolve<IFollow> {
  constructor(protected service: FollowService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFollow> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((follow: HttpResponse<Follow>) => {
          if (follow.body) {
            return of(follow.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Follow());
  }
}
