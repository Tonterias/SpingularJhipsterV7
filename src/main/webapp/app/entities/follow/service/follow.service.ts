import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFollow, getFollowIdentifier } from '../follow.model';

export type EntityResponseType = HttpResponse<IFollow>;
export type EntityArrayResponseType = HttpResponse<IFollow[]>;

@Injectable({ providedIn: 'root' })
export class FollowService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/follows');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(follow: IFollow): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(follow);
    return this.http
      .post<IFollow>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(follow: IFollow): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(follow);
    return this.http
      .put<IFollow>(`${this.resourceUrl}/${getFollowIdentifier(follow) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(follow: IFollow): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(follow);
    return this.http
      .patch<IFollow>(`${this.resourceUrl}/${getFollowIdentifier(follow) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IFollow>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFollow[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFollowToCollectionIfMissing(followCollection: IFollow[], ...followsToCheck: (IFollow | null | undefined)[]): IFollow[] {
    const follows: IFollow[] = followsToCheck.filter(isPresent);
    if (follows.length > 0) {
      const followCollectionIdentifiers = followCollection.map(followItem => getFollowIdentifier(followItem)!);
      const followsToAdd = follows.filter(followItem => {
        const followIdentifier = getFollowIdentifier(followItem);
        if (followIdentifier == null || followCollectionIdentifiers.includes(followIdentifier)) {
          return false;
        }
        followCollectionIdentifiers.push(followIdentifier);
        return true;
      });
      return [...followsToAdd, ...followCollection];
    }
    return followCollection;
  }

  protected convertDateFromClient(follow: IFollow): IFollow {
    return Object.assign({}, follow, {
      creationDate: follow.creationDate?.isValid() ? follow.creationDate.toJSON() : undefined,
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
      res.body.forEach((follow: IFollow) => {
        follow.creationDate = follow.creationDate ? dayjs(follow.creationDate) : undefined;
      });
    }
    return res;
  }
}
