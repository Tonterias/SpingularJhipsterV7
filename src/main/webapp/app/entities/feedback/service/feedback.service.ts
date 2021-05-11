import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFeedback, getFeedbackIdentifier } from '../feedback.model';

export type EntityResponseType = HttpResponse<IFeedback>;
export type EntityArrayResponseType = HttpResponse<IFeedback[]>;

@Injectable({ providedIn: 'root' })
export class FeedbackService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/feedbacks');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(feedback: IFeedback): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(feedback);
    return this.http
      .post<IFeedback>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(feedback: IFeedback): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(feedback);
    return this.http
      .put<IFeedback>(`${this.resourceUrl}/${getFeedbackIdentifier(feedback) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(feedback: IFeedback): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(feedback);
    return this.http
      .patch<IFeedback>(`${this.resourceUrl}/${getFeedbackIdentifier(feedback) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IFeedback>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFeedback[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFeedbackToCollectionIfMissing(feedbackCollection: IFeedback[], ...feedbacksToCheck: (IFeedback | null | undefined)[]): IFeedback[] {
    const feedbacks: IFeedback[] = feedbacksToCheck.filter(isPresent);
    if (feedbacks.length > 0) {
      const feedbackCollectionIdentifiers = feedbackCollection.map(feedbackItem => getFeedbackIdentifier(feedbackItem)!);
      const feedbacksToAdd = feedbacks.filter(feedbackItem => {
        const feedbackIdentifier = getFeedbackIdentifier(feedbackItem);
        if (feedbackIdentifier == null || feedbackCollectionIdentifiers.includes(feedbackIdentifier)) {
          return false;
        }
        feedbackCollectionIdentifiers.push(feedbackIdentifier);
        return true;
      });
      return [...feedbacksToAdd, ...feedbackCollection];
    }
    return feedbackCollection;
  }

  protected convertDateFromClient(feedback: IFeedback): IFeedback {
    return Object.assign({}, feedback, {
      creationDate: feedback.creationDate?.isValid() ? feedback.creationDate.toJSON() : undefined,
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
      res.body.forEach((feedback: IFeedback) => {
        feedback.creationDate = feedback.creationDate ? dayjs(feedback.creationDate) : undefined;
      });
    }
    return res;
  }
}
