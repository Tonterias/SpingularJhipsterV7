import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IConfigVariables, getConfigVariablesIdentifier } from '../config-variables.model';

export type EntityResponseType = HttpResponse<IConfigVariables>;
export type EntityArrayResponseType = HttpResponse<IConfigVariables[]>;

@Injectable({ providedIn: 'root' })
export class ConfigVariablesService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/config-variables');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(configVariables: IConfigVariables): Observable<EntityResponseType> {
    return this.http.post<IConfigVariables>(this.resourceUrl, configVariables, { observe: 'response' });
  }

  update(configVariables: IConfigVariables): Observable<EntityResponseType> {
    return this.http.put<IConfigVariables>(
      `${this.resourceUrl}/${getConfigVariablesIdentifier(configVariables) as number}`,
      configVariables,
      { observe: 'response' }
    );
  }

  partialUpdate(configVariables: IConfigVariables): Observable<EntityResponseType> {
    return this.http.patch<IConfigVariables>(
      `${this.resourceUrl}/${getConfigVariablesIdentifier(configVariables) as number}`,
      configVariables,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IConfigVariables>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IConfigVariables[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addConfigVariablesToCollectionIfMissing(
    configVariablesCollection: IConfigVariables[],
    ...configVariablesToCheck: (IConfigVariables | null | undefined)[]
  ): IConfigVariables[] {
    const configVariables: IConfigVariables[] = configVariablesToCheck.filter(isPresent);
    if (configVariables.length > 0) {
      const configVariablesCollectionIdentifiers = configVariablesCollection.map(
        configVariablesItem => getConfigVariablesIdentifier(configVariablesItem)!
      );
      const configVariablesToAdd = configVariables.filter(configVariablesItem => {
        const configVariablesIdentifier = getConfigVariablesIdentifier(configVariablesItem);
        if (configVariablesIdentifier == null || configVariablesCollectionIdentifiers.includes(configVariablesIdentifier)) {
          return false;
        }
        configVariablesCollectionIdentifiers.push(configVariablesIdentifier);
        return true;
      });
      return [...configVariablesToAdd, ...configVariablesCollection];
    }
    return configVariablesCollection;
  }
}
