import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICommunity, Community } from '../community.model';
import { CommunityService } from '../service/community.service';

@Injectable({ providedIn: 'root' })
export class CommunityRoutingResolveService implements Resolve<ICommunity> {
  constructor(protected service: CommunityService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICommunity> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((community: HttpResponse<Community>) => {
          if (community.body) {
            return of(community.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Community());
  }
}
