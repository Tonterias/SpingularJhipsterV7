import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUrllink, getUrllinkIdentifier } from '../urllink.model';

export type EntityResponseType = HttpResponse<IUrllink>;
export type EntityArrayResponseType = HttpResponse<IUrllink[]>;

@Injectable({ providedIn: 'root' })
export class UrllinkService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/urllinks');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(urllink: IUrllink): Observable<EntityResponseType> {
    return this.http.post<IUrllink>(this.resourceUrl, urllink, { observe: 'response' });
  }

  update(urllink: IUrllink): Observable<EntityResponseType> {
    return this.http.put<IUrllink>(`${this.resourceUrl}/${getUrllinkIdentifier(urllink) as number}`, urllink, { observe: 'response' });
  }

  partialUpdate(urllink: IUrllink): Observable<EntityResponseType> {
    return this.http.patch<IUrllink>(`${this.resourceUrl}/${getUrllinkIdentifier(urllink) as number}`, urllink, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUrllink>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUrllink[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addUrllinkToCollectionIfMissing(urllinkCollection: IUrllink[], ...urllinksToCheck: (IUrllink | null | undefined)[]): IUrllink[] {
    const urllinks: IUrllink[] = urllinksToCheck.filter(isPresent);
    if (urllinks.length > 0) {
      const urllinkCollectionIdentifiers = urllinkCollection.map(urllinkItem => getUrllinkIdentifier(urllinkItem)!);
      const urllinksToAdd = urllinks.filter(urllinkItem => {
        const urllinkIdentifier = getUrllinkIdentifier(urllinkItem);
        if (urllinkIdentifier == null || urllinkCollectionIdentifiers.includes(urllinkIdentifier)) {
          return false;
        }
        urllinkCollectionIdentifiers.push(urllinkIdentifier);
        return true;
      });
      return [...urllinksToAdd, ...urllinkCollection];
    }
    return urllinkCollection;
  }
}
