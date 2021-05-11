import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFrontpageconfig, getFrontpageconfigIdentifier } from '../frontpageconfig.model';

export type EntityResponseType = HttpResponse<IFrontpageconfig>;
export type EntityArrayResponseType = HttpResponse<IFrontpageconfig[]>;

@Injectable({ providedIn: 'root' })
export class FrontpageconfigService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/frontpageconfigs');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(frontpageconfig: IFrontpageconfig): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(frontpageconfig);
    return this.http
      .post<IFrontpageconfig>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(frontpageconfig: IFrontpageconfig): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(frontpageconfig);
    return this.http
      .put<IFrontpageconfig>(`${this.resourceUrl}/${getFrontpageconfigIdentifier(frontpageconfig) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(frontpageconfig: IFrontpageconfig): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(frontpageconfig);
    return this.http
      .patch<IFrontpageconfig>(`${this.resourceUrl}/${getFrontpageconfigIdentifier(frontpageconfig) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IFrontpageconfig>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFrontpageconfig[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFrontpageconfigToCollectionIfMissing(
    frontpageconfigCollection: IFrontpageconfig[],
    ...frontpageconfigsToCheck: (IFrontpageconfig | null | undefined)[]
  ): IFrontpageconfig[] {
    const frontpageconfigs: IFrontpageconfig[] = frontpageconfigsToCheck.filter(isPresent);
    if (frontpageconfigs.length > 0) {
      const frontpageconfigCollectionIdentifiers = frontpageconfigCollection.map(
        frontpageconfigItem => getFrontpageconfigIdentifier(frontpageconfigItem)!
      );
      const frontpageconfigsToAdd = frontpageconfigs.filter(frontpageconfigItem => {
        const frontpageconfigIdentifier = getFrontpageconfigIdentifier(frontpageconfigItem);
        if (frontpageconfigIdentifier == null || frontpageconfigCollectionIdentifiers.includes(frontpageconfigIdentifier)) {
          return false;
        }
        frontpageconfigCollectionIdentifiers.push(frontpageconfigIdentifier);
        return true;
      });
      return [...frontpageconfigsToAdd, ...frontpageconfigCollection];
    }
    return frontpageconfigCollection;
  }

  protected convertDateFromClient(frontpageconfig: IFrontpageconfig): IFrontpageconfig {
    return Object.assign({}, frontpageconfig, {
      creationDate: frontpageconfig.creationDate?.isValid() ? frontpageconfig.creationDate.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.creationDate = res.body.creationDate ? dayjs(res.body.creationDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((frontpageconfig: IFrontpageconfig) => {
        frontpageconfig.creationDate = frontpageconfig.creationDate ? dayjs(frontpageconfig.creationDate) : undefined;
      });
    }
    return res;
  }
}
