import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBlockuser, getBlockuserIdentifier } from '../blockuser.model';

export type EntityResponseType = HttpResponse<IBlockuser>;
export type EntityArrayResponseType = HttpResponse<IBlockuser[]>;

@Injectable({ providedIn: 'root' })
export class BlockuserService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/blockusers');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(blockuser: IBlockuser): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(blockuser);
    return this.http
      .post<IBlockuser>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(blockuser: IBlockuser): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(blockuser);
    return this.http
      .put<IBlockuser>(`${this.resourceUrl}/${getBlockuserIdentifier(blockuser) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(blockuser: IBlockuser): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(blockuser);
    return this.http
      .patch<IBlockuser>(`${this.resourceUrl}/${getBlockuserIdentifier(blockuser) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IBlockuser>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IBlockuser[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addBlockuserToCollectionIfMissing(
    blockuserCollection: IBlockuser[],
    ...blockusersToCheck: (IBlockuser | null | undefined)[]
  ): IBlockuser[] {
    const blockusers: IBlockuser[] = blockusersToCheck.filter(isPresent);
    if (blockusers.length > 0) {
      const blockuserCollectionIdentifiers = blockuserCollection.map(blockuserItem => getBlockuserIdentifier(blockuserItem)!);
      const blockusersToAdd = blockusers.filter(blockuserItem => {
        const blockuserIdentifier = getBlockuserIdentifier(blockuserItem);
        if (blockuserIdentifier == null || blockuserCollectionIdentifiers.includes(blockuserIdentifier)) {
          return false;
        }
        blockuserCollectionIdentifiers.push(blockuserIdentifier);
        return true;
      });
      return [...blockusersToAdd, ...blockuserCollection];
    }
    return blockuserCollection;
  }

  protected convertDateFromClient(blockuser: IBlockuser): IBlockuser {
    return Object.assign({}, blockuser, {
      creationDate: blockuser.creationDate?.isValid() ? blockuser.creationDate.toJSON() : undefined,
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
      res.body.forEach((blockuser: IBlockuser) => {
        blockuser.creationDate = blockuser.creationDate ? dayjs(blockuser.creationDate) : undefined;
      });
    }
    return res;
  }
}
