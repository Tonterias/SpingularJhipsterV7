import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICactivity, getCactivityIdentifier } from '../cactivity.model';

export type EntityResponseType = HttpResponse<ICactivity>;
export type EntityArrayResponseType = HttpResponse<ICactivity[]>;

@Injectable({ providedIn: 'root' })
export class CactivityService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/cactivities');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(cactivity: ICactivity): Observable<EntityResponseType> {
    return this.http.post<ICactivity>(this.resourceUrl, cactivity, { observe: 'response' });
  }

  update(cactivity: ICactivity): Observable<EntityResponseType> {
    return this.http.put<ICactivity>(`${this.resourceUrl}/${getCactivityIdentifier(cactivity) as number}`, cactivity, {
      observe: 'response',
    });
  }

  partialUpdate(cactivity: ICactivity): Observable<EntityResponseType> {
    return this.http.patch<ICactivity>(`${this.resourceUrl}/${getCactivityIdentifier(cactivity) as number}`, cactivity, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICactivity>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICactivity[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCactivityToCollectionIfMissing(
    cactivityCollection: ICactivity[],
    ...cactivitiesToCheck: (ICactivity | null | undefined)[]
  ): ICactivity[] {
    const cactivities: ICactivity[] = cactivitiesToCheck.filter(isPresent);
    if (cactivities.length > 0) {
      const cactivityCollectionIdentifiers = cactivityCollection.map(cactivityItem => getCactivityIdentifier(cactivityItem)!);
      const cactivitiesToAdd = cactivities.filter(cactivityItem => {
        const cactivityIdentifier = getCactivityIdentifier(cactivityItem);
        if (cactivityIdentifier == null || cactivityCollectionIdentifiers.includes(cactivityIdentifier)) {
          return false;
        }
        cactivityCollectionIdentifiers.push(cactivityIdentifier);
        return true;
      });
      return [...cactivitiesToAdd, ...cactivityCollection];
    }
    return cactivityCollection;
  }
}
