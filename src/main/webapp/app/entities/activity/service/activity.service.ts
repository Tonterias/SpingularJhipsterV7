import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IActivity, getActivityIdentifier } from '../activity.model';

export type EntityResponseType = HttpResponse<IActivity>;
export type EntityArrayResponseType = HttpResponse<IActivity[]>;

@Injectable({ providedIn: 'root' })
export class ActivityService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/activities');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(activity: IActivity): Observable<EntityResponseType> {
    return this.http.post<IActivity>(this.resourceUrl, activity, { observe: 'response' });
  }

  update(activity: IActivity): Observable<EntityResponseType> {
    return this.http.put<IActivity>(`${this.resourceUrl}/${getActivityIdentifier(activity) as number}`, activity, { observe: 'response' });
  }

  partialUpdate(activity: IActivity): Observable<EntityResponseType> {
    return this.http.patch<IActivity>(`${this.resourceUrl}/${getActivityIdentifier(activity) as number}`, activity, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IActivity>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IActivity[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addActivityToCollectionIfMissing(activityCollection: IActivity[], ...activitiesToCheck: (IActivity | null | undefined)[]): IActivity[] {
    const activities: IActivity[] = activitiesToCheck.filter(isPresent);
    if (activities.length > 0) {
      const activityCollectionIdentifiers = activityCollection.map(activityItem => getActivityIdentifier(activityItem)!);
      const activitiesToAdd = activities.filter(activityItem => {
        const activityIdentifier = getActivityIdentifier(activityItem);
        if (activityIdentifier == null || activityCollectionIdentifiers.includes(activityIdentifier)) {
          return false;
        }
        activityCollectionIdentifiers.push(activityIdentifier);
        return true;
      });
      return [...activitiesToAdd, ...activityCollection];
    }
    return activityCollection;
  }
}
