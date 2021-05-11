import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBlockuser, Blockuser } from '../blockuser.model';
import { BlockuserService } from '../service/blockuser.service';

@Injectable({ providedIn: 'root' })
export class BlockuserRoutingResolveService implements Resolve<IBlockuser> {
  constructor(protected service: BlockuserService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBlockuser> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((blockuser: HttpResponse<Blockuser>) => {
          if (blockuser.body) {
            return of(blockuser.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Blockuser());
  }
}
