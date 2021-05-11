import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICeleb, getCelebIdentifier } from '../celeb.model';

export type EntityResponseType = HttpResponse<ICeleb>;
export type EntityArrayResponseType = HttpResponse<ICeleb[]>;

@Injectable({ providedIn: 'root' })
export class CelebService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/celebs');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(celeb: ICeleb): Observable<EntityResponseType> {
    return this.http.post<ICeleb>(this.resourceUrl, celeb, { observe: 'response' });
  }

  update(celeb: ICeleb): Observable<EntityResponseType> {
    return this.http.put<ICeleb>(`${this.resourceUrl}/${getCelebIdentifier(celeb) as number}`, celeb, { observe: 'response' });
  }

  partialUpdate(celeb: ICeleb): Observable<EntityResponseType> {
    return this.http.patch<ICeleb>(`${this.resourceUrl}/${getCelebIdentifier(celeb) as number}`, celeb, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICeleb>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICeleb[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCelebToCollectionIfMissing(celebCollection: ICeleb[], ...celebsToCheck: (ICeleb | null | undefined)[]): ICeleb[] {
    const celebs: ICeleb[] = celebsToCheck.filter(isPresent);
    if (celebs.length > 0) {
      const celebCollectionIdentifiers = celebCollection.map(celebItem => getCelebIdentifier(celebItem)!);
      const celebsToAdd = celebs.filter(celebItem => {
        const celebIdentifier = getCelebIdentifier(celebItem);
        if (celebIdentifier == null || celebCollectionIdentifiers.includes(celebIdentifier)) {
          return false;
        }
        celebCollectionIdentifiers.push(celebIdentifier);
        return true;
      });
      return [...celebsToAdd, ...celebCollection];
    }
    return celebCollection;
  }
}
