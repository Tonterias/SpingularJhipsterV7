import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAppphoto, getAppphotoIdentifier } from '../appphoto.model';

export type EntityResponseType = HttpResponse<IAppphoto>;
export type EntityArrayResponseType = HttpResponse<IAppphoto[]>;

@Injectable({ providedIn: 'root' })
export class AppphotoService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/appphotos');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(appphoto: IAppphoto): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(appphoto);
    return this.http
      .post<IAppphoto>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(appphoto: IAppphoto): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(appphoto);
    return this.http
      .put<IAppphoto>(`${this.resourceUrl}/${getAppphotoIdentifier(appphoto) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(appphoto: IAppphoto): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(appphoto);
    return this.http
      .patch<IAppphoto>(`${this.resourceUrl}/${getAppphotoIdentifier(appphoto) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAppphoto>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAppphoto[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAppphotoToCollectionIfMissing(appphotoCollection: IAppphoto[], ...appphotosToCheck: (IAppphoto | null | undefined)[]): IAppphoto[] {
    const appphotos: IAppphoto[] = appphotosToCheck.filter(isPresent);
    if (appphotos.length > 0) {
      const appphotoCollectionIdentifiers = appphotoCollection.map(appphotoItem => getAppphotoIdentifier(appphotoItem)!);
      const appphotosToAdd = appphotos.filter(appphotoItem => {
        const appphotoIdentifier = getAppphotoIdentifier(appphotoItem);
        if (appphotoIdentifier == null || appphotoCollectionIdentifiers.includes(appphotoIdentifier)) {
          return false;
        }
        appphotoCollectionIdentifiers.push(appphotoIdentifier);
        return true;
      });
      return [...appphotosToAdd, ...appphotoCollection];
    }
    return appphotoCollection;
  }

  protected convertDateFromClient(appphoto: IAppphoto): IAppphoto {
    return Object.assign({}, appphoto, {
      creationDate: appphoto.creationDate?.isValid() ? appphoto.creationDate.toJSON() : undefined,
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
      res.body.forEach((appphoto: IAppphoto) => {
        appphoto.creationDate = appphoto.creationDate ? dayjs(appphoto.creationDate) : undefined;
      });
    }
    return res;
  }
}
