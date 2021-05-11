import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICceleb, getCcelebIdentifier } from '../cceleb.model';

export type EntityResponseType = HttpResponse<ICceleb>;
export type EntityArrayResponseType = HttpResponse<ICceleb[]>;

@Injectable({ providedIn: 'root' })
export class CcelebService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/ccelebs');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(cceleb: ICceleb): Observable<EntityResponseType> {
    return this.http.post<ICceleb>(this.resourceUrl, cceleb, { observe: 'response' });
  }

  update(cceleb: ICceleb): Observable<EntityResponseType> {
    return this.http.put<ICceleb>(`${this.resourceUrl}/${getCcelebIdentifier(cceleb) as number}`, cceleb, { observe: 'response' });
  }

  partialUpdate(cceleb: ICceleb): Observable<EntityResponseType> {
    return this.http.patch<ICceleb>(`${this.resourceUrl}/${getCcelebIdentifier(cceleb) as number}`, cceleb, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICceleb>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICceleb[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCcelebToCollectionIfMissing(ccelebCollection: ICceleb[], ...ccelebsToCheck: (ICceleb | null | undefined)[]): ICceleb[] {
    const ccelebs: ICceleb[] = ccelebsToCheck.filter(isPresent);
    if (ccelebs.length > 0) {
      const ccelebCollectionIdentifiers = ccelebCollection.map(ccelebItem => getCcelebIdentifier(ccelebItem)!);
      const ccelebsToAdd = ccelebs.filter(ccelebItem => {
        const ccelebIdentifier = getCcelebIdentifier(ccelebItem);
        if (ccelebIdentifier == null || ccelebCollectionIdentifiers.includes(ccelebIdentifier)) {
          return false;
        }
        ccelebCollectionIdentifiers.push(ccelebIdentifier);
        return true;
      });
      return [...ccelebsToAdd, ...ccelebCollection];
    }
    return ccelebCollection;
  }
}
