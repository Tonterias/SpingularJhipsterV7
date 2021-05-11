import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFeedback, Feedback } from '../feedback.model';
import { FeedbackService } from '../service/feedback.service';

@Injectable({ providedIn: 'root' })
export class FeedbackRoutingResolveService implements Resolve<IFeedback> {
  constructor(protected service: FeedbackService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFeedback> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((feedback: HttpResponse<Feedback>) => {
          if (feedback.body) {
            return of(feedback.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Feedback());
  }
}
