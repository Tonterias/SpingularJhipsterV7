import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICinterest, getCinterestIdentifier } from '../cinterest.model';

export type EntityResponseType = HttpResponse<ICinterest>;
export type EntityArrayResponseType = HttpResponse<ICinterest[]>;

@Injectable({ providedIn: 'root' })
export class CinterestService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/cinterests');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(cinterest: ICinterest): Observable<EntityResponseType> {
    return this.http.post<ICinterest>(this.resourceUrl, cinterest, { observe: 'response' });
  }

  update(cinterest: ICinterest): Observable<EntityResponseType> {
    return this.http.put<ICinterest>(`${this.resourceUrl}/${getCinterestIdentifier(cinterest) as number}`, cinterest, {
      observe: 'response',
    });
  }

  partialUpdate(cinterest: ICinterest): Observable<EntityResponseType> {
    return this.http.patch<ICinterest>(`${this.resourceUrl}/${getCinterestIdentifier(cinterest) as number}`, cinterest, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICinterest>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICinterest[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCinterestToCollectionIfMissing(
    cinterestCollection: ICinterest[],
    ...cinterestsToCheck: (ICinterest | null | undefined)[]
  ): ICinterest[] {
    const cinterests: ICinterest[] = cinterestsToCheck.filter(isPresent);
    if (cinterests.length > 0) {
      const cinterestCollectionIdentifiers = cinterestCollection.map(cinterestItem => getCinterestIdentifier(cinterestItem)!);
      const cinterestsToAdd = cinterests.filter(cinterestItem => {
        const cinterestIdentifier = getCinterestIdentifier(cinterestItem);
        if (cinterestIdentifier == null || cinterestCollectionIdentifiers.includes(cinterestIdentifier)) {
          return false;
        }
        cinterestCollectionIdentifiers.push(cinterestIdentifier);
        return true;
      });
      return [...cinterestsToAdd, ...cinterestCollection];
    }
    return cinterestCollection;
  }
}
