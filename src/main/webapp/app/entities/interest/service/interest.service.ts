import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IInterest, getInterestIdentifier } from '../interest.model';

export type EntityResponseType = HttpResponse<IInterest>;
export type EntityArrayResponseType = HttpResponse<IInterest[]>;

@Injectable({ providedIn: 'root' })
export class InterestService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/interests');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(interest: IInterest): Observable<EntityResponseType> {
    return this.http.post<IInterest>(this.resourceUrl, interest, { observe: 'response' });
  }

  update(interest: IInterest): Observable<EntityResponseType> {
    return this.http.put<IInterest>(`${this.resourceUrl}/${getInterestIdentifier(interest) as number}`, interest, { observe: 'response' });
  }

  partialUpdate(interest: IInterest): Observable<EntityResponseType> {
    return this.http.patch<IInterest>(`${this.resourceUrl}/${getInterestIdentifier(interest) as number}`, interest, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IInterest>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IInterest[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addInterestToCollectionIfMissing(interestCollection: IInterest[], ...interestsToCheck: (IInterest | null | undefined)[]): IInterest[] {
    const interests: IInterest[] = interestsToCheck.filter(isPresent);
    if (interests.length > 0) {
      const interestCollectionIdentifiers = interestCollection.map(interestItem => getInterestIdentifier(interestItem)!);
      const interestsToAdd = interests.filter(interestItem => {
        const interestIdentifier = getInterestIdentifier(interestItem);
        if (interestIdentifier == null || interestCollectionIdentifiers.includes(interestIdentifier)) {
          return false;
        }
        interestCollectionIdentifiers.push(interestIdentifier);
        return true;
      });
      return [...interestsToAdd, ...interestCollection];
    }
    return interestCollection;
  }
}
