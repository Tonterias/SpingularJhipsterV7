import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAppuser, getAppuserIdentifier } from '../appuser.model';

export type EntityResponseType = HttpResponse<IAppuser>;
export type EntityArrayResponseType = HttpResponse<IAppuser[]>;

@Injectable({ providedIn: 'root' })
export class AppuserService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/appusers');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(appuser: IAppuser): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(appuser);
    return this.http
      .post<IAppuser>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(appuser: IAppuser): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(appuser);
    return this.http
      .put<IAppuser>(`${this.resourceUrl}/${getAppuserIdentifier(appuser) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(appuser: IAppuser): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(appuser);
    return this.http
      .patch<IAppuser>(`${this.resourceUrl}/${getAppuserIdentifier(appuser) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAppuser>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAppuser[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAppuserToCollectionIfMissing(appuserCollection: IAppuser[], ...appusersToCheck: (IAppuser | null | undefined)[]): IAppuser[] {
    const appusers: IAppuser[] = appusersToCheck.filter(isPresent);
    if (appusers.length > 0) {
      const appuserCollectionIdentifiers = appuserCollection.map(appuserItem => getAppuserIdentifier(appuserItem)!);
      const appusersToAdd = appusers.filter(appuserItem => {
        const appuserIdentifier = getAppuserIdentifier(appuserItem);
        if (appuserIdentifier == null || appuserCollectionIdentifiers.includes(appuserIdentifier)) {
          return false;
        }
        appuserCollectionIdentifiers.push(appuserIdentifier);
        return true;
      });
      return [...appusersToAdd, ...appuserCollection];
    }
    return appuserCollection;
  }

  protected convertDateFromClient(appuser: IAppuser): IAppuser {
    return Object.assign({}, appuser, {
      creationDate: appuser.creationDate?.isValid() ? appuser.creationDate.toJSON() : undefined,
      birthdate: appuser.birthdate?.isValid() ? appuser.birthdate.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.creationDate = res.body.creationDate ? dayjs(res.body.creationDate) : undefined;
      res.body.birthdate = res.body.birthdate ? dayjs(res.body.birthdate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((appuser: IAppuser) => {
        appuser.creationDate = appuser.creationDate ? dayjs(appuser.creationDate) : undefined;
        appuser.birthdate = appuser.birthdate ? dayjs(appuser.birthdate) : undefined;
      });
    }
    return res;
  }
}
