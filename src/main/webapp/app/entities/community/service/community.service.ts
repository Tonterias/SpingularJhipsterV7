import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICommunity, getCommunityIdentifier } from '../community.model';

export type EntityResponseType = HttpResponse<ICommunity>;
export type EntityArrayResponseType = HttpResponse<ICommunity[]>;

@Injectable({ providedIn: 'root' })
export class CommunityService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/communities');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(community: ICommunity): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(community);
    return this.http
      .post<ICommunity>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(community: ICommunity): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(community);
    return this.http
      .put<ICommunity>(`${this.resourceUrl}/${getCommunityIdentifier(community) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(community: ICommunity): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(community);
    return this.http
      .patch<ICommunity>(`${this.resourceUrl}/${getCommunityIdentifier(community) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICommunity>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICommunity[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCommunityToCollectionIfMissing(
    communityCollection: ICommunity[],
    ...communitiesToCheck: (ICommunity | null | undefined)[]
  ): ICommunity[] {
    const communities: ICommunity[] = communitiesToCheck.filter(isPresent);
    if (communities.length > 0) {
      const communityCollectionIdentifiers = communityCollection.map(communityItem => getCommunityIdentifier(communityItem)!);
      const communitiesToAdd = communities.filter(communityItem => {
        const communityIdentifier = getCommunityIdentifier(communityItem);
        if (communityIdentifier == null || communityCollectionIdentifiers.includes(communityIdentifier)) {
          return false;
        }
        communityCollectionIdentifiers.push(communityIdentifier);
        return true;
      });
      return [...communitiesToAdd, ...communityCollection];
    }
    return communityCollection;
  }

  protected convertDateFromClient(community: ICommunity): ICommunity {
    return Object.assign({}, community, {
      creationDate: community.creationDate?.isValid() ? community.creationDate.toJSON() : undefined,
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
      res.body.forEach((community: ICommunity) => {
        community.creationDate = community.creationDate ? dayjs(community.creationDate) : undefined;
      });
    }
    return res;
  }
}
